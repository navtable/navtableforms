#!/usr/bin/env python

# Module to create classes related to jgoodies validation framework (model, binding & validator).
# @author: Andres Maneiro

import os, sys
from Cheetah.Template import Template

import TemplatesVars
from TemplatesDefinition import ModelTemplate, ModelValidatorTemplate, BindingTemplate
from parserAbeille import ParserAbeille

from xml.sax import make_parser
sys.path.append('../createlayer')

from parserORMLite import XMLSAXParser

class CreateJavaValidationModel:
    """Define the methods to create the classes related to jgoodies validation framework (model & validator).

       Parameters:
       - argv[1]:  output directory
       - argv[2:]: all abeille files
    """

    varstringlist  = []
    varbooleanlist = []
    varintlist     = []
    varreallist    = []
    outputdir      = ""

    def __init__(self, outputdir, xmlFileName, abeillefiles):
        self.outputdir = os.path.dirname(outputdir)
        self.xmlFileName = xmlFileName

        # It takes as files as we wanted
        for i in range(len(abeillefiles)):
            self.getVarListsFromAbeille(abeillefiles[i])

    def getVarListsFromAbeille(self, abeillefile):
        """Get from an Abeille file all variables to create the model: FTFs, TFs, TA, CHB & CB.

        - self.varstringlist contains all widgets (JFormattedTextField, JTextField, JTextArea & JComboBoxes) to be represented as a _String_ in the model.
        - self.varbooleanlist contains all widgets (JCheckBoxes) to be represented as _boolean_ in the model.

        Other two list are kept (self.varintlist & self.varrealist) to build the validation class,
        all components in one of those are also in self.varstringlist.
        """

        pa = ParserAbeille(abeillefile)
        widgets = pa.getWidgetsWithContent()

        for i in range(len(widgets)):
            aux = widgets[i].split('.')
            if (aux[1].upper() == 'CHB'):
                self.varbooleanlist.append(aux[0].replace(" ", ""))
            else:
                self.varstringlist.append(str(aux[0]).replace(" ", ""))

            # if the var has type checking, add it too to its own type array
            # (varreallit or varintlit)
            if(len(aux) == 3):
                if (aux[2].upper()   == "INT") or (aux[2].upper() == "INTEGER") :
                    self.varintlist.append(self.varstringlist[-1])
                elif (aux[2].upper() == "REAL"):
                    self.varreallist.append(self.varstringlist[-1])


    def getVarStringList(self):
        """Return a list of all variables to build the model. All of them will be represented as _String_ in the model."""
        return self.varstringlist

    def getVarBooleanList(self):
        """Return a list of variables to build the model. All of these vars are going to be represented as _boolean_ in the model."""
        return self.varbooleanlist

    def getSqlitePath(self):
        return TemplatesVars.sqlitepath

    def getEnumLayersFromSqlite(self):
        import sqlite3
        dbpath = self.getSqlitePath()
        conn = sqlite3.connect(dbpath)

        values = []
        tables = TemplatesVars.enumlayers
        enumlayers = {}
        for i in range(len(tables)):
            c = conn.execute("PRAGMA table_info(" + tables[i] + ")")
            aux = c.fetchall()
            for j in range(len(aux)):
                values.append(aux[j][1])
            enumlayers[tables[i]] = values

        return enumlayers

    def getEnumLayers(self):
        """Return a dictionary keeping the names of the tables and their fields:
           enumlayers = {'table1': [field1, field2, field3], 'table2': [field1, field2]}"""


        values = []
        tables = TemplatesVars.enumlayers
        enumlayers = {}

        parser = make_parser()
        xmlParser = XMLSAXParser()
        parser.setContentHandler(xmlParser)
        parser.parse(open(self.xmlFileName))

        ls = xmlParser.getLayerSet()
        layers = ls.getLayerSet()

        for l in range(len(layers)):
            if layers[l].getLayerName() in tables:
                recordset = layers[l].getRecordSet()
                for r in range(len(recordset)):
                    values.append(recordset[r]['name'])
                enumlayers[layers[l].getLayerName()] = values

        return enumlayers

    def getNonWidgetVars(self):
        """Return a list containing all vars not related to a widget"""
        return TemplatesVars.varsnonwidget

    def getVarIntList(self):
        """Return a list of all variables (FTFs) with INTEGER checking."""
        return self.varintlist

    def getVarRealList(self):
        """Return a list of all variables (FTFs) with DOUBLE checking."""
        return self.varreallist

    def getModelTemplate(self):
        """Return model template file name from TemplatesVars."""
        return TemplatesVars.templatemodel

    def getValidatorTemplate(self):
        """Return validator template file name from TemplatesVars."""
        return TemplatesVars.templatevalidator

    def getBindingTemplate(self):
        """Return binding template file name from TemplatesVars."""
        return TemplatesVars.templatebinding

    def getPackageName(self):
        """Return package name from TemplatesVars."""
        return TemplatesVars.pkgnamevalidation

    def getClassModelName(self):
        """Return class model name from TemplatesVars."""
        return TemplatesVars.classmodel

    def getObjectModelName(self):
        """Return object model name from TemplatesVars."""
        return TemplatesVars.objectmodel

    def getClassValidatorName(self):
        """Return class validator name from TemplatesVars."""
        return TemplatesVars.classvalidator

    def getClassBindingName(self):
        return TemplatesVars.classbinding

    def createModel(self):
        """Create the model class for jgoodies validation framework."""

        print "Generating java code for model ........................"
        templatefile    = self.getModelTemplate()
        f               = open(templatefile, 'rw')
        template        = f.read()
        t               = ModelTemplate(template)

        t.packagename   = self.getPackageName()
        t.classmodel    = self.getClassModelName()
        t.varsstring    = self.getVarStringList()
        t.varsint       = self.getVarIntList()
        t.varsreal      = self.getVarRealList()
        t.varsboolean   = self.getVarBooleanList()
        t.varsnonwidget = self.getNonWidgetVars()
        t.enumlayers    = self.getEnumLayers()

        javacontents    = t.__str__()

        modelfile       = self.outputdir + "/" + self.getClassModelName() + ".java"
        f2              = open(modelfile, 'w')
        f2.write(javacontents)
        f.close()
        f2.close()

        print modelfile + " created!"


    def createValidator(self):
        """Create the validation class for jgoodies validation framework."""

        print "Generating java code for validation ........................"

        templatefile     = self.getValidatorTemplate()
        f                = open(templatefile, 'rw')
        template         = f.read()
        t                = ModelValidatorTemplate(template)

        t.packagename    = self.getPackageName()
        t.classvalidator = self.getClassValidatorName()
        t.classmodel     = self.getClassModelName()
        t.objectmodel    = self.getObjectModelName()
        t.varsint        = self.getVarIntList()
        t.varsreal       = self.getVarRealList()

        javacontents     = t.__str__()

        validatorfile    = self.outputdir + "/" + self.getClassValidatorName() + ".java"
        f2               = open(validatorfile, 'w')
        f2.write(javacontents)
        f.close()
        f2.close()

        print validatorfile + " DONE!"

    def createBinding(self):
        """Create the binding class for jgoodies validation framework."""

        print "Generating java code for binding .........................."

        templatefile = self.getBindingTemplate()
        f            = open(templatefile,'rw')
        template     = f.read()
        t            = BindingTemplate(template)

        t.packagename    = self.getPackageName()
        t.classbinding   = self.getClassBindingName()
        t.classmodel     = self.getClassModelName()
        t.objectmodel    = self.getObjectModelName()
        t.classvalidator = self.getClassValidatorName()

        javacontents = t.__str__()

        bindingfile  = self.outputdir + "/" + self.getClassBindingName() + ".java"
        f2           = open(bindingfile, 'w')
        f2.write(javacontents)
        f.close()
        f2.close()

        print bindingfile + " DONE!"



if __name__ == "__main__":
    """Execute the module to create the classes for jgoodies validation framework.

    Parameters:
    - argv[1]:  output directory
    - argv[2]: xml data model definition
    - argv[3:]: abeille files
    """

    import sys

    outputdir    = sys.argv[1]
    xmlFileName  = sys.argv[2]
    abeillefiles = sys.argv[3:]

    newmodel = CreateJavaValidationModel(outputdir, xmlFileName, abeillefiles)
    newmodel.createModel()
    newmodel.createValidator()
    newmodel.createBinding()
