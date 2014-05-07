package es.icarto.gvsig.navtableforms.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.testutils.FLyrVectStub;
import es.udc.cartolab.gvsig.testutils.MapControlStub;

public class TOCLayerManagerTest {

    @Test
    public void testGetLayerByName() {
	FLyrVect lyrVect = new FLyrVectStub("test");
	MapControlStub mapControl = new MapControlStub();
	mapControl.addLayer(lyrVect);
	
	FLayers group = new FLayers();
	group.setName("test group");
	FLyrVect vectLyrInGroup = new FLyrVectStub("inner vect layer");
	group.addLayer(vectLyrInGroup);
	mapControl.addLayer(group);
	
	FLayers sameNameGroup = new FLayers();
	sameNameGroup.setName("group and layer have same name");
	FLyrVect sameNameLyr = new FLyrVectStub("group and layer have same name");
	sameNameGroup.addLayer(sameNameLyr);
	mapControl.addLayer(sameNameGroup);
	
	TOCLayerManager tocLayerManager = new TOCLayerManager(mapControl);
	
	FLyrVect notFound = tocLayerManager.getLayerByName("test group");
	assertNull(notFound);
	
	FLyrVect testLyrVect = tocLayerManager.getLayerByName("test");
	assertEquals(testLyrVect, lyrVect);
	
	FLyrVect groupedLayer = tocLayerManager.getLayerByName("group and layer have same name");
	assertEquals(groupedLayer, sameNameLyr);
	
    }

    @Test
    public void testHasName() {
	FLyrVect lyrVect = new FLyrVectStub("test");
	MapControlStub mapControl = new MapControlStub();
	mapControl.addLayer(lyrVect);
	
	TOCLayerManager tocLayerManager = new TOCLayerManager(mapControl);
	assertTrue(tocLayerManager.hasName(lyrVect, "test"));
    }

    @Test
    public void testIsFLyrVect() {
	FLyrVect lyrVect = new FLyrVectStub("test");
	MapControlStub mapControl = new MapControlStub();
	mapControl.addLayer(lyrVect);

	TOCLayerManager tocLayerManager = new TOCLayerManager(mapControl);
	assertTrue(tocLayerManager.isFLyrVect(lyrVect));
    }

    @Test
    public void testIsFLayers() {
	FLyrVect lyrVect = new FLyrVectStub("test");
	MapControlStub mapControl = new MapControlStub();
	mapControl.addLayer(lyrVect);
	
	FLayers group = new FLayers();
	group.setName("test group");
	FLyrVect vectLyrInGroup = new FLyrVectStub("inner vect layer");
	group.addLayer(vectLyrInGroup);
	mapControl.addLayer(group);
	
	TOCLayerManager tocLayerManager = new TOCLayerManager(mapControl);
	assertTrue(tocLayerManager.isFLayers(group));
    }

}
