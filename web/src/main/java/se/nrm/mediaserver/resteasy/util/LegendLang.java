/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.mediaserver.resteasy.util;

import java.io.Serializable;
import javax.ws.rs.FormParam;

/**
 *
 * @author ingimar
 */
public class LegendLang implements Serializable {

    

    @FormParam("lang")
    private String language;

    @FormParam("legend")
    private String legend;

    public LegendLang(String language, String legend) {
        this.language = language;
        this.legend = legend;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    private static final long serialVersionUID = 5113590440030922223L;

}
