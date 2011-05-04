#!/usr/bin/python
# -*- coding: utf-8 -*-

# Module to create XML file
# @author Andrés Maneiro <amaneiro@icarto.es>

from XMLTemplate import XMLTemplate

class XMLBuilder():

    def __init__(self):
        self.layeralias = "example"
        self.layername = "example"
        self.tablename = "example"
        self.layergeom = "example"
        self.recordset = [
            {'fieldname': "example",
             'fieldtype': "example",
             'fieldlength': "example",
             'fielddecimalcount': "example",
             'defaultvalue': "example",
             'fieldalias': "example"
             },
            {'fieldname': "example",
             'fieldtype': "example",
             'fieldlength': "example",
             'fielddecimalcount': "example",
             'defaultvalue': "example",
             'fieldalias': "example"
             }
        ]

    def setLayerAlias(self, alias):
        self.layeralias = alias

    def setLayerName(self, name):
        self.layername = name

    def setTableName(self, name):
        self.tablename = name

    def setLayerGeom(self, geomtype):
        self.layergeom = geomtype

    def setRecordSet(self, recordset):
        self.recordset = recordset

    def createXML(self, templatefile, outputfile):
        finput = open(templatefile, 'r')
        foutput = open(outputfile, 'w')

        template = finput.read()
        t = XMLTemplate(template)
        t.layeralias = self.layeralias
        t.layername  = self.layername
        t.tablename  = self.tablename
        t.layergeom  = self.layergeom
        t.recordset  = self.recordset
        xmlcontents = t.__str__()
        foutput.write(xmlcontents)

        foutput.close()
        finput.close()

if __name__ == "__main__":

    builder = XMLBuilder()
    builder.createXML('./templates/XML.tmpl', '/tmp/example.xml')

