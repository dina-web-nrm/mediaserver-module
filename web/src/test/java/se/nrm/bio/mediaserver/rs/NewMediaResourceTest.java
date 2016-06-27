/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.rs;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import se.nrm.bio.mediaserver.business.StartupBean;

/**
 *
 * @author ingimar
 */
public class NewMediaResourceTest {

    public NewMediaResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException {

    }

    @Test @Ignore
    public void testGetImageByDimension() {
//        String validUUID = "863ec044-17cf-4c87-81cc-783ab13230ae";
//        String mimeFormat = "image/png";
//        int height = 60;
//
//        StartupBean envBean = mock(StartupBean.class);
//        ConcurrentHashMap map = new ConcurrentHashMap();
//        map.put("path_to_files", "/opt/data/mediaserver/demo/");
//        when(envBean.getEnvironment()).thenReturn(map);
//
//        NewMediaResource instance = new NewMediaResource();
//        instance.envBean = envBean;
//        Response result = instance.getImageByDimension(validUUID, mimeFormat, height);
//        int actual = result.getStatus();
//        final int EXPECTED = 200;
//        assertEquals(EXPECTED, actual);
    }
}
