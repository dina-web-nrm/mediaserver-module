/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.bio.mediaserver.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.nrm.bio.mediaserver.domain.Image;
import se.nrm.bio.mediaserver.domain.Media;

/**
 *
 * @author ingimar
 */
public class TagHelperTest {
    
    public TagHelperTest() {
    }

    @Test
    public void testAddingTags() {
        System.out.println("addingTags");
        Media media = new Image();
        String tags = "view:left&music:reggea";
        TagHelper instance = new TagHelper();
        List<String> expResult = new ArrayList<>();
        expResult.add("view:left");
        expResult.add("music:reggea");
        List<String> result = instance.addingTags(media, tags);
        assertEquals(expResult, result);
    }

}
