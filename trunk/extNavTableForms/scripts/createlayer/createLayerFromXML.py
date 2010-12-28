#!/usr/bin/python

import sys
from osgeo import ogr
import string
from xml.sax import make_parser

from parserORMLite import XMLSAXParser

#define my own exceptions
class InvalidGeometryTypeFromXML(Exception): pass
class InvalidFieldTypeFromXML(Exception): pass

class CreateESRIShapeFileFromXML():

    def __init__(self, xmlFileName):
        parser = make_parser()
        self.xmlParser = XMLSAXParser()
        parser.setContentHandler(self.xmlParser)
        parser.parse(open(xmlFileName))

    def __getGeometryType(self, typeFromXML):
        print typeFromXML
        if (typeFromXML.upper() == "POINT"):
            return ogr.wkbPoint
        elif (typeFromXML.upper() == "POLYGON"):
            return ogr.wkbPolygon
        elif (typeFromXML.upper() == "LINE"):
            return ogr.wkbLineString
        else:
            raise InvalidGeometryTypeFromXML(typeFromXML)

    def __getFieldType(self, typeFromXML):
        if (typeFromXML.upper() == "INTEGER"):
            return ogr.OFTInteger
        elif (typeFromXML.upper() == "STRING"):
            return ogr.OFTString
        elif (typeFromXML.upper() == "BOOLEAN"):
            return ogr.OFTString
        elif (typeFromXML.upper() == "REAL"):
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
        name = self.getFileName()
        ds = drv.CreateDataSource(name)
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
            if(str(field['type'].lower()) == 'boolean'):
                field_defn.SetWidth(5)
            else:
                field_defn.SetWidth(int(field['length']))
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
    lyrCreator = CreateESRIShapeFileFromXML(sys.argv[1])
    lyrCreator.createLayers()
