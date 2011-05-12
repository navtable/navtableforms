#!/usr/bin/python

from BeautifulSoup import BeautifulStoneSoup
from xml.sax import make_parser
from xml.sax.handler import ContentHandler

class Database():

    def __init__(self):
        self.tables = []

    def addTableToDatabase(self, table):
        self.tables.append(table)

    def getTables(self):
        return self.tables

class Table():

    def __init__(self):
        self.primaryKey = []
        self.tableName = ""
        self.fields = []
        self.databaseName = ""

    def setDatabaseName(self, name):
        self.databaseName = name

    def getDatabaseName(self):
        return self.databaseName

    def addFieldToTable(self, fieldName, fieldType, fieldLength, fieldPrecision):
        newField = {'name': fieldName,
                    'type': fieldType,
                    'length': fieldLength,
                    'precision': fieldPrecision}
        self.fields.append(newField)

    def getFields(self):
        return self.fields

    def getTableName(self):
        return self.tableName

    def setTableName(self, name):
        self.tableName = name

    def getPrimaryKey(self):
        return self.primaryKey

    def setPrimaryKey(self, keys):
        for key in keys:
            self.primaryKey.append(key)

class Layer():

    def __init__(self):
        self.__layerName = ''
        self.__geometryType = ''
        self.__recordSet = []

    def getFileName(self):
        return self.__layerName + ".shp"

    def getLayerName(self):
        return self.__layerName

    def setLayerName(self, name):
        self.__layerName = name

    def getGeometryType(self):
        return self.__geometryType

    def setGeometryType(self, geomType):
        self.__geometryType = geomType

    def getRecordSet(self):
        return self.__recordSet

    def addFieldToRecordSet(self, fieldName, fieldType, fieldLength, fieldPrecision):
        newField = {
            'name': fieldName,
            'type': fieldType,
            'length': fieldLength,
            'precision': fieldPrecision}
        self.__recordSet.append(newField)

class LayerSet():

    def __init__(self):
        self.layer_set = []

    def addLayer(self, layer):
        self.layer_set.append(layer)

    def getLayerSet(self):
        return self.layer_set

class XMLSAXParser(ContentHandler):

    def __init__(self):
        self.node = ""
        self.layerSet = LayerSet()
        self.database = Database()
        self.tmpLayer = None
        self.tmpTable = None
        self.tmpFieldName = ""
        self.tmpFieldType = ""
        self.tmpFieldLength = ""
        self.tmpFieldDecimalCount = ""
        self.tmpValue = ""

    def setNode(self, name):
        self.node = name

    def getNode(self):
        return self.node

    def startElement(self, name, attrs):
        if name.lower() == "layer":
            self.tmpLayer = Layer()
            self.tmpTable = Table()
        elif name.lower() == "tablename":
            self.setNode(name.lower())
        elif name.lower() == "nameoflayer":
            self.setNode(name.lower())
        elif name.lower() == "geometry":
            self.setNode(name.lower())
        elif name.lower() == "fieldname":
            self.setNode(name.lower())
        elif name.lower() == "fieldtype":
            self.setNode(name.lower())
        elif name.lower() == "fieldlength":
            self.setNode(name.lower())
        elif name.lower() == "fielddecimalcount":
            self.setNode(name.lower())


    def characters(self, ch):
        self.tmpValue = ch

    def endElement(self, name):
        if name.lower() == self.getNode():
            self.executeAction(name.lower())
        elif name.lower() == "field":
            self.tmpLayer.addFieldToRecordSet(
                self.tmpFieldName,
                self.tmpFieldType,
                self.tmpFieldLength,
                self.tmpFieldDecimalCount)
            self.tmpTable.addFieldToTable(
                self.tmpFieldName,
                self.tmpFieldType,
                self.tmpFieldLength,
                self.tmpFieldDecimalCount)
        elif name.lower() == "layer":
            self.layerSet.addLayer(self.tmpLayer)
            self.database.addTableToDatabase(self.tmpTable)

    def executeAction(self, action):
        #print "Action: " + action + " with value: " + self.tmpValue
        if action == "nameoflayer":
            self.tmpLayer.setLayerName(self.tmpValue)
        if action == "tablename":
            self.tmpTable.setTableName(self.tmpValue)
        elif action == "geometry":
            self.tmpLayer.setGeometryType(self.tmpValue)
        elif action == "fieldname":
            self.tmpFieldName = self.tmpValue
        elif action == "fieldtype":
            self.tmpFieldType = self.tmpValue
        elif action == "fieldlength":
            self.tmpFieldLength = self.tmpValue
        elif action == "fielddecimalcount":
            self.tmpFieldDecimalCount = self.tmpValue

    def getLayerSet(self):
        return self.layerSet

    def getDatabase(self):
        return self.database


if __name__ == "__main__":

    parser = make_parser()
    myParser = XMLSAXParser()
    parser.setContentHandler(myParser)
    parser.parse(open("ArqueoPonte.xml"))

    ls = myParser.getLayerSet()
    layers = ls.getLayerSet()
    print len(layers)
    for layer in layers:
        print len(layer.getRecordSet())

    db = myParser.getDatabase()
    tables = db.getTables()
    print "Nro tables: " + str(len(tables))
    for table in tables:
        name = table.getTableName()
        print name + ": " + str(len(table.getFields()))

