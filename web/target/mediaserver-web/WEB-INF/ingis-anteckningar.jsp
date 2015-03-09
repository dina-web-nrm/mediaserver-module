

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        Gjort:
        1. använder web.xml
        2. använder wildfly 8.2 ( ej testat 8.1)
        
        2. tomma beans.xml -> CDI -> se jpassion.com
        
        obs 2: tagit bort från  from Media-klassen tills vidare : Såg felet när jag körde NewClass -
        obs 1:  Jag hade heller inte med 'exif'-kolumnen i Image-tabellen 
        
        @OneToMany(cascade = CascadeType.ALL, mappedBy = "media",targetEntity = MediaText.class, fetch = FetchType.EAGER)
        @XmlElementWrapper(name = "descriptions")
        @XmlElement(name = "description", required = true)
        private Set<MediaText> texts = new HashSet<>(0);
        
        - nu funkar multipart ( web.xml införd igen )
        
        Ska göras:
        - lyft in jsp-sida för post
        - posta, funkar eller inte ?
        - Kolla mediatext
        -
    </body>
    
</html>
