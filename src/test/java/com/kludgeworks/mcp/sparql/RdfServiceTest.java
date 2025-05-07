package com.kludgeworks.mcp.sparql;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RdfServiceTest {

    final String endpoint = "https://publications.europa.eu/webapi/rdf/sparql";

    private RdfService service;

    @BeforeEach
    void setUp() {
        service = new RdfService();
    }

    @Test
    void getWorkUri() throws IOException {
        String query = """
                PREFIX cdm: <http://publications.europa.eu/ontology/cdm#>
                PREFIX cmr: <http://publications.europa.eu/ontology/cdm/cmr#>
                PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
                
                SELECT DISTINCT ?work ?p ?o
                WHERE {
                  # First get our work
                  ?work cdm:resource_legal_id_celex "32015R1998"^^xsd:string .
                }
                """;

        JsonNode json = service.select(endpoint, query);
        System.out.println(json);
    }

    @Test
    void getExpressionsFromWorkItem() throws IOException {
        String query = """
                PREFIX cdm: <http://publications.europa.eu/ontology/cdm#>
                PREFIX cmr: <http://publications.europa.eu/ontology/cdm/cmr#>
                PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
                
                SELECT DISTINCT ?expression ?p ?o
                WHERE {
                  # First get our work
                  ?work cdm:resource_legal_id_celex "32015R1998"^^xsd:string .
                
                  # Find expressions that reference this work and are in English
                  ?expression cdm:expression_belongs_to_work ?work ;
                              cmr:lang "eng"^^xsd:language ;
                             ?p ?o .
                }
                """;

        JsonNode json = service.select(endpoint, query);
        System.out.println(json);
    }

    @Test
    void getExpression() throws IOException {
        String query = """
                PREFIX cdm: <http://publications.europa.eu/ontology/cdm#>
                PREFIX cmr: <http://publications.europa.eu/ontology/cdm/cmr#>
                PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
                
                SELECT DISTINCT ?expression ?p ?o
                WHERE {
                  # First get our work
                  ?work cdm:resource_legal_id_celex "32015R1998"^^xsd:string .
                
                  # Find expressions that reference this work and are in English
                  ?expression cdm:expression_belongs_to_work ?work ;
                              cmr:lang "eng"^^xsd:language ;
                             ?p ?o .
                }
                """;

        JsonNode json = service.select(endpoint, query);
        System.out.println(json);
    }

    @Test
    void describe() throws IOException {
        String query = """
                    DESCRIBE <http://publications.europa.eu/resource/cellar/c38f361d-8a99-11e5-b8b7-01aa75ed71a1>
                """;
        JsonNode json = service.describe(endpoint, query);
        System.out.println(json);
    }
}
