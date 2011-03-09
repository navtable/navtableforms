#!/usr/bin/python

import sys, os
from osgeo import ogr
import string
from xml.sax import make_parser

from parserORMLite import XMLSAXParser

#define my own exceptions
class InvalidGeometryTypeFromXML(Exception): pass
class InvalidFieldTypeFromXML(Exception): pass

class CreateESRIShapeFileFromXML():

    def __init__(self, xmlFileName, dataFolderPath=None):

        if dataFolderPath != None:
            self.shpFolderPath = dataFolderPath + '/SHP/'
            self.dbfFolderPath = dataFolderPath + '/DBF/'
            self.ensureDir(dataFolderPath)
        else:
            self.shpFolderPath = './'
            self.dbfFolderPath = './'

        parser = make_parser()
        self.xmlParser = XMLSAXParser()
        parser.setContentHandler(self.xmlParser)
        parser.parse(open(xmlFileName))


    def ensureDir(self, f):
        if os.path.isdir(f):
            sys.stderr.write(f + ' already exists. Remove it before continue\n')
            sys.exit()
        else:
            os.makedirs(self.shpFolderPath)
            os.makedirs(self.dbfFolderPath)


    def __getGeometryType(self, typeFromXML):
        print typeFromXML
        if (typeFromXML.upper() == "POINT"):
            return ogr.wkbPoint
        elif (typeFromXML.upper() == "POLYGON"):
            return ogr.wkbPolygon
        elif (typeFromXML.upper() == "LINE"):
            return ogr.wkbLineString
        elif (typeFromXML.upper() == "ALPHANUMERIC"):
            return ogr.wkbNone
        else:
            raise InvalidGeometryTypeFromXML(typeFromXML)

    def __getFieldType(self, typeFromXML):
        if (typeFromXML.upper() == "INTEGER"):
            return ogr.OFTInteger
        elif (typeFromXML.upper() == "STRING"):
            return ogr.OFTString
        elif (typeFromXML.upper() == "BOOLEAN"):
            return ogr.OFTString
        elif (typeFromXML.upper() == "DOUBLE"):
            return ogr.OFTReal
        else:
            raise InvalidFieldTypeFromXML(typeFromXML)

    def getLayerName(self):
        return self.layerName

    def getLayerType(self):
        return self.layerType

    def getFieldsArray(self):
        return self.fieldsArray

    def getFileName(self):
        return self.fileName

    def getDriver(self):
        driverName = "ESRI Shapefile"
        drv = ogr.GetDriverByName(driverName)
        if drv is None:
            print "%s driver not available.\n" % driverName
            sys.exit( 1 )
        print "Driver OK"
        return drv

    def getDataSource(self):
        drv = self.getDriver()
        #ds = None
        filePath = self.getFileName()

        if self.dbfFolderPath != "":
            if self.layerType == ogr.wkbNone:
                filePath = self.dbfFolderPath + filePath
            else:
                filePath = self.shpFolderPath + filePath

        ds = drv.CreateDataSource(filePath)
        if ds is None:
            print "Creation of output file failed.\n"
            sys.exit( 1 )
        print "Datasource OK"
        return ds

    def createLayer(self):
        ds = self.getDataSource()
        layerName = self.getLayerName()
        layerType = self.getLayerType()
        lyr = ds.CreateLayer(layerName, None, layerType)
        if lyr is None:
            print "Layer creation failed.\n"
            sys.exit( 1 )
        print "Layer OK"

        fields = self.getFieldsArray()
        for field in fields:
            #lyr = self.getLayer()
            field_defn = ogr.FieldDefn(
                str(field['name']),
                self.__getFieldType(str(field['type'])))
            field_defn.SetWidth(int(field['length']))
            field_defn.SetPrecision(int(field['precision']))

            if lyr.CreateField(field_defn) != 0:
                print "Creating Name field failed.\n"
                sys.exit( 1 )

    def createLayers(self):
        ls = self.xmlParser.getLayerSet()
        layers = ls.getLayerSet()
        for layer in layers:
            self.fileName = str(layer.getFileName())
            self.layerName = str(layer.getLayerName())
            self.fieldsArray = layer.getRecordSet()

            self.layerType = self.__getGeometryType(str(layer.getGeometryType()))
            self.createLayer()

if __name__ == "__main__":
    import sys

    if len(sys.argv) == 2:
        lyrCreator = CreateESRIShapeFileFromXML(sys.argv[1])
    elif len(sys.argv) == 3:
        lyrCreator = CreateESRIShapeFileFromXML(sys.argv[1], sys.argv[2])
    else:
        sys.exit('Error, incorrect number of input parameters')
    lyrCreator.createLayers()
