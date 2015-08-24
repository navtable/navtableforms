package es.icarto.gvsig.navtableforms.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.iver.cit.gvsig.fmap.layers.FLayerStatus;
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
	FLyrVect sameNameLyr = new FLyrVectStub(
		"group and layer have same name");
	sameNameGroup.addLayer(sameNameLyr);
	mapControl.addLayer(sameNameGroup);

	TOCLayerManager tocLayerManager = new TOCLayerManager(mapControl);

	FLyrVect notFound = tocLayerManager.getLayerByName("test group");
	assertNull(notFound);

	FLyrVect testLyrVect = tocLayerManager.getLayerByName("test");
	assertEquals(testLyrVect, lyrVect);

	FLyrVect groupedLayer = tocLayerManager
		.getLayerByName("group and layer have same name");
	assertEquals(groupedLayer, sameNameLyr);

    }

    @Test
    public void testGetJoinedLayers() {
	FLyrVect lyrVect = new FLyrVectStub("test");
	lyrVect.setIsJoined(true);
	MapControlStub mapControl = new MapControlStub();
	mapControl.addLayer(lyrVect);

	FLayers group = new FLayers();
	group.setName("test group");
	FLyrVect vectLyrInGroup = new FLyrVectStub("inner vect layer");
	group.addLayer(vectLyrInGroup);
	mapControl.addLayer(group);

	FLayers sameNameGroup = new FLayers();
	final String sameName = "group and layer have same name";
	sameNameGroup.setName(sameName);
	FLyrVect sameNameLyr = new FLyrVectStub(sameName);
	sameNameLyr.setIsJoined(true);
	sameNameGroup.addLayer(sameNameLyr);
	mapControl.addLayer(sameNameGroup);

	TOCLayerManager tocLayerManager = new TOCLayerManager(mapControl);

	List<FLyrVect> expected = Arrays.asList(lyrVect, sameNameLyr);
	List<FLyrVect> actual = tocLayerManager.getJoinedLayers();
	assertTrue(cmp(expected, actual));
    }

    @Test
    public void testGetEditingLayers() {
	FLayerStatus status = new FLayerStatus();
	status.editing = true;

	FLyrVect lyrVect = new FLyrVectStub("test");
	MapControlStub mapControl = new MapControlStub();
	mapControl.addLayer(lyrVect);

	FLayers group = new FLayers();
	group.setName("test group");
	FLyrVect vectLyrInGroup = new FLyrVectStub("inner vect layer");
	vectLyrInGroup.setFLayerStatus(status);
	group.addLayer(vectLyrInGroup);
	mapControl.addLayer(group);

	FLayers sameNameGroup = new FLayers();
	final String sameName = "group and layer have same name";
	sameNameGroup.setName(sameName);
	FLyrVect sameNameLyr = new FLyrVectStub(sameName);
	sameNameLyr.setFLayerStatus(status);
	sameNameGroup.addLayer(sameNameLyr);
	mapControl.addLayer(sameNameGroup);

	TOCLayerManager tocLayerManager = new TOCLayerManager(mapControl);

	List<FLyrVect> expected = Arrays.asList(vectLyrInGroup, sameNameLyr);
	List<FLyrVect> actual = tocLayerManager.getEditingLayers();
	assertTrue(cmp(expected, actual));
    }

    private static <E> boolean cmp(List<E> l1, List<E> l2) {
	// make a copy of the list so the original list is not changed, and
	// remove() is supported
	List<E> cp = new ArrayList<E>(l1);
	for (Object o : l2) {
	    if (!cp.remove(o)) {
		return false;
	    }
	}
	return cp.isEmpty();
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
