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


import sys
from XMLTemplate import XMLTemplate

class XMLFileTools():

    # name, type, length, decimalcount, defaultvalue, alias
    defaults = {
        "string": ['', 'VARCHAR', 50, 0, '', ''],
        "integer": ['', 'INTEGER', 12, 0, '', ''],
        "double":  ['', 'DOUBLE',  18, 6, '', ''],
        "boolean": ['', 'BOOLEAN',  0, 0, '', ''],
        "date":    ['', 'DATE',     0, 0, '', '']
    }

    admittableStringsForPointType = ['POINT', 'PUNTO']
    admittableStringsForLineType = ['LINE', 'LINEA', 'LÍNEA']
    admittableStringsForPolygonType = ['POLYGON', 'POLIGONO', 'POLÍGONO']
    admitableStringsForAlphanumericType = ['ALPHANUMERIC', 'ALFANUMERICO', 'ALFANUMÉRICO', 'ALFANUMERICA', 'ALFANUMÉRICA']

    admittableStringsForIntegerType = ['INTEGER', 'ENTERO','INT']
    admittableStringsForRealType = ['REAL', 'DOUBLE']
    admittableStringsForStringType = ['STRING', 'TEXT','TEXTO']
    admittableStringsForBooleanType = ['BOOLEAN', 'BOOL','BOOLEANO']
    admittableStringsForDateType = ['DATE', 'FECHA']



    def __init__(self):

        templateFile = 'TemplateXML.tmpl'
        self.f1 = open(templateFile, 'rw')
        template = self.f1.read()
        self._recordset = []
        self._template = XMLTemplate(template)

    def __del(self):
        self.f1.close()


    def getDefaultLength(self, fieldtype):
        return self.defaults[fieldtype][2]

    def getDefaultDecimalCount(self, fieldtype):
        return self.defaults[fieldtype][3]

    def getDefaultValue(self, fieldtype):
        return self.defaults[fieldtype][4]

    def getDefaultAlias(self, fieldtype):
        return self.defaults[fieldtype][5]

    def setLayerAlias(self, layerAlias):
        self._template.layeralias = layerAlias

    def setLayerName(self, layerName):
        self._template.layername = layerName

    def setLayerGeom(self, layerGeom):
        if layerGeom.upper() in self.admittableStringsForPointType:
            normativizedLayerGeom = 'point'
        elif layerGeom.upper() in self.admittableStringsForLineType:
            normativizedLayerGeom = 'line'
        elif layerGeom.upper() in self.admittableStringsForPolygonType:
            normativizedLayerGeom = 'polygon'
        elif layerGeom.upper() in self.admitableStringsForAlphanumericType:
            normativizedLayerGeom = 'alphanumeric'
        else:
            sys.stderr.write('setLayerGeom: Something bad happen!\n')
            sys.exit(-1)

        self._template.layergeom = normativizedLayerGeom

    def setTableName(self, tableName):
        self._template.tablename = tableName

    def setPrimaryKey(self, primaryKey):
        self._template.primarykey = primaryKey


    def getNormativizedFieldType(self, fieldType):
        if fieldType.upper() in self.admittableStringsForIntegerType:
            normativizedFieldType = 'integer'
        elif fieldType.upper() in self.admittableStringsForRealType:
            normativizedFieldType = 'double'
        elif fieldType.upper() in self.admittableStringsForStringType:
            normativizedFieldType = 'string'
        elif fieldType.upper() in self.admittableStringsForBooleanType:
            normativizedFieldType = 'boolean'
        elif fieldType.upper() in self.admittableStringsForDateType:
            normativizedFieldType = 'date'
        else:
            sys.stderr.write('getNormativizedFieldType: Something bad happen!\n')
            sys.exit(-1)


        return normativizedFieldType


    def setField(self, fieldname, fieldtype):
        normativizedFieldType = self.getNormativizedFieldType(fieldtype)
        self._recordset.append([
            fieldname,
            normativizedFieldType,
            self.getDefaultLength(normativizedFieldType),
            self.getDefaultDecimalCount(normativizedFieldType),
            self.getDefaultValue(normativizedFieldType),
            self.getDefaultAlias(normativizedFieldType)
        ])

    def setRecordset (self):
        self._template.recordset = self._recordset

    def setFirstTag (self, firstTag):
        self._template.firstTag = firstTag

    def setLayerTag (self, layerTag):
        self._template.layerTag = layerTag

    def setLastTag (self, lastTag):
        self._template.lastTag = lastTag

    def getTemplateAsStr(self):
        return str(self._template)
