#!`which python`
# -*- coding: utf-8 -*-
#
# Copyright (C) 2010 CartoLab. Universidade de A Coruña
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 3 of the License, or
# any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


import csv, sys
from XMLFileTools import XMLFileTools

class CSVParser():

    def __init__(self, file):
        self.f = open(file, 'r')
        self._csv = csv.reader(self.f,delimiter=';')
        self.header = {}


    def __del__(self):
        self.f.close()



    def discardNRows(self, n):
        for i in range(n):
            self._csv.next()



    def getGeometryTypeColumn(self):
        return self.header['geometrytype']

    def getLayerNameColumn(self):
        return self.header['layername']

    def getFieldNameColumn(self):
        return self.header['fieldname']

    def getFieldTypeColumn(self):
        return self.header['fieldtype']



    def setIndexOfColumnHeaders(self, row):
        for i in range(len(row)):
            cmpField = row[i].strip().upper()

            if cmpField == 'TIPO CAPA':
                self.header['geometrytype'] = i

            elif cmpField == 'CAPA':
                self.header['layername'] = i

            elif cmpField == 'NOMBRE':
                self.header['fieldname'] = i

            elif cmpField == 'DESCRIPCIÓN':
                self.header['fielddescription'] = i

            elif cmpField == 'TIPO':
                self.header['fieldtype'] = i



    def addNewLayerTags(self, row):
        self._template.setLayerAlias(row[self.getLayerNameColumn()])
        self._template.setLayerName(row[self.getLayerNameColumn()])
        self._template.setTableName(row[self.getLayerNameColumn()])
        self._template.setLayerGeom(row[self.getGeometryTypeColumn()])
        self._template.setPrimaryKey(' ')
        self._template.setFirstTag(False)
        self._template.setLayerTag(True)
        self._template.setLastTag(False)



    def addNewFieldTags(self, row):
        self._template.setFirstTag(False)
        self._template.setLayerTag(True)
        self._template.setLastTag(False)
        fieldname = row[self.getFieldNameColumn()]
        fieldtype = row[self.getFieldTypeColumn()].upper()

        if fieldname == '' or fieldtype == '':
            sys.stderr.write('Empty field ignored\n')
        else:
            self._template.setField(fieldname, fieldtype)


    def getFirstTag(self):
        self._template.setFirstTag(True)
        self._template.setLayerTag(False)
        self._template.setLastTag(False)
        return self._template.getTemplateAsStr()

    def getLastTag(self):
        self._template.setFirstTag(False)
        self._template.setLayerTag(False)
        self._template.setLastTag(True)
        return self._template.getTemplateAsStr()



    def createLayerDefinitionXMLFile(self):
        oldLayerName = ''

        self.setIndexOfColumnHeaders(self._csv.next())

        self._template = XMLFileTools()
        xml = self.getFirstTag()
        for row in self._csv:
            actualLayerName = row[self.getLayerNameColumn()]

            if actualLayerName != oldLayerName:
                if oldLayerName != '':
                    self._template.setRecordset()
                    xml = xml + self._template.getTemplateAsStr()
                oldLayerName = row[self.getLayerNameColumn()]
                self._template = XMLFileTools()
                self.addNewLayerTags(row)
                self.addNewFieldTags(row)

            elif actualLayerName == oldLayerName:
                self.addNewFieldTags(row)
            else:
                print 'Something bad happens :('
                exit
        self._template.setRecordset()
        xml = xml + self._template.getTemplateAsStr() + self.getLastTag()

        print xml


if __name__ == "__main__":
    createXML = CSVParser(sys.argv[1])
    # createXML.discardNRows(1)
    createXML.createLayerDefinitionXMLFile()
