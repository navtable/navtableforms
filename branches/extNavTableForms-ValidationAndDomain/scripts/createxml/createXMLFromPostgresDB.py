#!/usr/bin/python
# -*- coding: utf-8 -*-

# Module to create XML file from a Postgres Database
# @author Andrés Maneiro <amaneiro@icarto.es>

import psycopg2
from XMLBuilder import XMLBuilder
import ParamsForPostgressBuilder

class XMLFromPostgressFactory():

    def __init__(self):
        self.gvsigdefaults = {
            'string':{
                'length': 50,
                'decimalcount': 0,
                'defaultvalue': ''
            },
            'integer':{
                'length': 12,
                'decimalcount': 0,
                'defaultvalue': ''
            },
            'double':{
                'length': 18,
                'decimalcount': 6,
                'defaultvalue': ''
            },
            'boolean':{
                'length': 0,
                'decimalcount': 0,
                'defaultvalue': ''
            },
            'date':{
                'length': 0,
                'decimalcount': 0,
                'defaultvalue': ''
            }
        }

        self.postgres2gvsig = {
            'character varying': "string",
            'integer': "integer",
            'double': "double",
            'double precision': "double",
            'boolean': "boolean",
            'date': "date"
        }

        self.layeralias = ""
        self.layername = ""
        self.tablename = ""
        self.layergeom = ""
        self.recordset = []
        self.setMetaInformation()
        self.setRecordSet()

    def setMetaInformation(self):
        self.tablename  = ParamsForPostgressBuilder.tablename
        self.layeralias = ParamsForPostgressBuilder.layeralias
        self.layername  = ParamsForPostgressBuilder.layername
        self.layergeom  = ParamsForPostgressBuilder.layergeom

    def getFieldLength(self, length, type):
        if (length != None):
            return length
        return self.gvsigdefaults[type]['length']

    def setRecordSet(self):
        host   = ParamsForPostgressBuilder.host
        db     = ParamsForPostgressBuilder.database
        user   = ParamsForPostgressBuilder.user
        schema = ParamsForPostgressBuilder.schema
        table  = ParamsForPostgressBuilder.tablename

        conn = psycopg2.connect(host=host, database=db, user=user)
        cur = conn.cursor()
        cur.execute("SELECT column_name, data_type, character_maximum_length " +
                    "FROM information_schema.columns " +
                    "WHERE table_schema = %s AND table_name = %s AND data_type <> 'USER-DEFINED'",
                    (schema, table))

        for row in cur.fetchall():
            self.recordset.append({
                'fieldname': row[0],
                'fieldtype': self.postgres2gvsig[row[1]],
                'fieldlength': self.getFieldLength(row[2], self.postgres2gvsig[row[1]]),
                'fielddecimalcount': self.gvsigdefaults[self.postgres2gvsig[row[1]]]['decimalcount'],
                'defaultvalue': self.gvsigdefaults[self.postgres2gvsig[row[1]]]['defaultvalue'],
                'fieldalias': row[0]
             })

    def createXML(self):
        builder = XMLBuilder()
        builder.setLayerName(self.layername)
        builder.setTableName(self.tablename)
        builder.setLayerAlias(self.layeralias)
        builder.setLayerGeom(self.layergeom)
        builder.setRecordSet(self.recordset)
        builder.createXML(ParamsForPostgressBuilder.templatefile, ParamsForPostgressBuilder.outputfile)

if __name__ == "__main__":
    factory = XMLFromPostgressFactory()
    factory.createXML()

