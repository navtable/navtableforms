#!/usr/bin/env python

# Module to create code from Abeille xml forms in an automated way.

import os

import TemplatesVars
from TemplatesDefinition import JavaCodeTemplateVars
from parserAbeille import ParserAbeille

class CreateJavaFormFromAbeille:
    """
    Module to create code from Abeille xml forms in an automated way.

    Parameters:
    - argv[1]: abeille xml file to process
    - argv[2]: output directory, if not provided uses the directory where is the xml file
    """

    def getTemplateName(self):
        return TemplatesVars.templateform

    def getPrimaryKeys(self):
        return TemplatesVars.pks

    def getPackageName(self):
        return TemplatesVars.pkgnameform

    def getPackageNameValidation(self):
        return TemplatesVars.pkgnamevalidation

    def getUseSqlite(self):
        return TemplatesVars.sqlite

    def getClassForm(self):
        return TemplatesVars.classform

    def getEnumLayers(self):
        return TemplatesVars.enumlayers

    def getFormName(self):
        return TemplatesVars.formname

    def getFormTitle(self):
        return TemplatesVars.formtitle

    def getClassModel(self):
        return TemplatesVars.classmodel

    def getObjectModel(self):
        return TemplatesVars.objectmodel

    def getClassBinding(self):
        return TemplatesVars.classbinding

    def getObjectBinding(self):
        return TemplatesVars.objectbinding

    def getClassValidator(self):
        return TemplatesVars.classvalidator

    def getLayerInXml(self):
	return TemplatesVars.layerinxml

    def execute(self, abeillefile, outputdir = ''):

        filename = os.path.basename(abeillefile)
        basedir  = os.path.dirname(abeillefile)

        pa = ParserAbeille(abeillefile)

        if outputdir == '':
            outputdir = basedir

        javafile = outputdir + '/' + self.getClassForm() + '.java'
        print javafile
        print basedir

        primarykeystext = self.getPrimaryKeys()
        pks = primarykeystext.split(',')
        for i in range(len(pks)) :
            pks[i] = pks[i].replace(' ','')

        writer = open(javafile, 'wb')


        # GETTING INFO FROM ABEILLE FILE --------------------------------------------------

        components = {'CB' : 'JComboBox',
                      'TF' : 'JTextField',
                      'FTF': 'JFormattedTextField',
                      'TA' : 'JTextArea',
                      'TB' : 'JTable',
                      'BT' : 'JButton',
                      'RB' : 'JRadioButton',
                      'CHB': 'JCheckBox'}

        ws1 = pa.getWidgetsWithoutChecking() # i.e.: ws1[0] = "nameofcomponent.CB"
        ws2 = pa.getWidgetsWithChecking()    # i.e.: ws2[0] = "nameofcomponent.FTF.INT"
        nro_ws1 = len(ws1)
        nro_widgets = nro_ws1 + len(ws2)

        widget          = [None] * nro_widgets # nameofcomponentCB
        widget_name     = [None] * nro_widgets # nameofcomponent
        widget_type     = [None] * nro_widgets # TFT, CB, ...
        widget_checking = [None] * nro_widgets # INT, ... (None if there is no checking)

        id_name         = [None] * nro_widgets # ID_NAMEOFCOMPONENT
        id_string       = [None] * nro_widgets # nameofcomponent.TFT.INT
        idx_name        = [None] * nro_widgets # nameofcomponent_idx

        hasbuttons      = pa.hasButtons()
        hastables       = pa.hasTables()
        buttonwidgets   = []
        tablewidgets    = []
        table_names     = {}
        table_pks       = {}
        # table_dbfs      = {}
        #table_links     = {}

        for i in range(nro_ws1):
            #widget_checking[i] = None #already initialized
            aux            = ws1[i].split('.') # ws1 has UTF8 strings
            widget_name[i] = str(aux[0])
            widget_type[i] = str(aux[1])

            widget[i] = widget_name[i] + widget_type[i]
            widget_type[i] = components[widget_type[i]]

            id_string[i]  = str(ws1[i])

        for i in range(len(ws2)):
            ioffset = i + nro_ws1 # take into account widgets already introduced by ws1 to calculate the index
            aux                      = ws2[i].split('.') # ws2 has UTF8 strings
            widget_name[ioffset]     = str(aux[0])
            widget_type[ioffset]     = str(aux[1])
            if(len(aux) == 3):
                widget_checking[ioffset] = str(aux[2])
            else:
                widget_checking[ioffset] = None

            widget[ioffset]      = widget_name[ioffset] + widget_type[ioffset]
            widget_type[ioffset] = components[widget_type[ioffset]]
            id_string[ioffset]   = str(ws2[i])

        for i in range(nro_widgets):
            id_name[i]  = "ID_" + widget_name[i].upper()
            idx_name[i] = widget_name[i].lower() + "_idx"

            #table_names[widget[i]] = None
            #table_dbfs[widget[i]]  = None
            #table_pks[widget[i]]   = None
            aux = ""
            aux_list = []
            if widget_type[i] == 'JTable':
                aux = raw_input("\n\nSQL Table name for " + widget_name[i] + ": ")
                table_names[widget[i]] = aux
                tablewidgets.append(widget_name[i])

                #tablelinks[widget[i]] = raw_input('Insert field to link the table: ')
                aux = raw_input("Insert primary keys, separated by commas: ")
                aux_list = aux.split(',')
                for j in range(len(aux_list)): #here change the values of widget[i]
                    aux_list[j] = aux_list[j].replace(' ','')
                table_pks[widget[i]] = aux_list

                # aux = raw_input("DBF file name (just name, not path) for "+ widget_name[i] + ": ")
                # if aux.lower().endswith('.dbf') :
                #    aux = aux[:aux.lower().index('.dbf')]
                # table_dbfs[widget[i]] = aux
            elif widget_type[i] == 'JButton':
                buttonwidgets.append(widget_name[i])

        # GENERATE JAVA FORM CLASS -------------------------------------------------
        print "Generating java code from abeille xml ........................................."

        file = self.getTemplateName()
        f = open(file, 'rw')
        template = f.read()
        t = JavaCodeTemplateVars(template)

        t.packagename    = self.getPackageName()
        t.pkgnamevalidation = self.getPackageNameValidation()
        t.sqlite         = self.getUseSqlite()
        t.classform      = self.getClassForm()
        t.enumlayers     = self.getEnumLayers()
        t.formname       = self.getFormName()
        t.formtitle      = self.getFormTitle()
	t.layerinxml     = self.getLayerInXml()

        t.classmodel     = self.getClassModel()
        t.objectmodel    = self.getObjectModel()
        t.classbinding   = self.getClassBinding()
        t.objectbinding  = self.getObjectBinding()
        t.classvalidator = self.getClassValidator()

        t.nro_widgets    = nro_widgets
        t.hastables      = hastables
        t.hasbuttons     = hasbuttons

        t.widget         = widget
        t.widget_name    = widget_name
        t.widget_type    = widget_type

        t.id_name        = id_name
        t.id_string      = id_string
        t.idx_name       = idx_name

        t.table_names    = table_names
        t.table_pks      = table_pks
        #t.table_dbfs     = table_dbfs
        t.pks            = pks

        t.tablewidgets   = tablewidgets
        t.buttonwidgets  = buttonwidgets

        javacontents = t.__str__()
        f2 = open(javafile, 'w')
        f2.write(javacontents)
        print javafile + "Done!"


if __name__ == "__main__":
    """Execute the code to create a class from an abeille file.

    Parameters:
    - argv[1]: xml file to process
    - argv[2]: output directory, if not provided uses the directory where is the xml file
    """

    import sys

    xmlfile = sys.argv[1]
    if len(sys.argv) == 3:
        outputdir = sys.argv[2]
    else:
        outputdir = ''

    newform = CreateJavaFormFromAbeille()
    newform.execute(xmlfile, outputdir)
