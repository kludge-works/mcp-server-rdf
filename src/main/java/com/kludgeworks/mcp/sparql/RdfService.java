package com.kludgeworks.mcp.sparql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class RdfService {

    @Tool(description = "Run a SELECT query against a RDF datastore")
    public JsonNode select(
    String serviceUrl, String query) throws IOException {
        try (QueryExecution qExec = QueryExecution.service(serviceUrl).query(query).build()) {
            ResultSet resultSet = qExec.execSelect();
            return toJson(resultSet);
        }
    }

    @Tool(description = "Run a DESCRIBE query against a RDF datastore")
    public JsonNode describe(String serviceUrl, String query) throws IOException {
        try (QueryExecution qExec = QueryExecution.service(serviceUrl).query(query).build()) {
            Model model = qExec.execDescribe();
            return toJson(model);
        }
    }

    private static JsonNode toJson(ResultSet rs) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(baos, rs);

        return map(baos);
    }

    private static JsonNode toJson(Model model) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        model.write(baos, Lang.RDFJSON.getName());

        return map(baos);
    }

    private static JsonNode map(ByteArrayOutputStream baos) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(baos.toByteArray());
    }

}
