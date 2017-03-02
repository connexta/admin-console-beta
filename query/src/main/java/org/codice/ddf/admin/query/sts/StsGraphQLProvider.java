package org.codice.ddf.admin.query.sts;

import static org.codice.ddf.admin.query.graphql.GraphQLCommons.fieldToGraphQLObjectType;
import static org.codice.ddf.admin.query.graphql.GraphQLCommons.fieldsToGraphQLFieldDefinition;

import java.util.Collection;
import java.util.HashMap;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.servlet.GraphQLMutationProvider;
import graphql.servlet.GraphQLQueryProvider;

public class StsGraphQLProvider implements GraphQLQueryProvider, GraphQLMutationProvider {
    @Override
    public GraphQLObjectType getQuery() {
        return fieldToGraphQLObjectType(new StsActionHandler());
    }

    @Override
    public String getName() {
        return new StsActionHandler().fieldName();
    }

    @Override
    public Object context() {
        return new HashMap<>();
    }

    @Override
    public Collection<GraphQLFieldDefinition> getMutations() {
        return fieldsToGraphQLFieldDefinition(new StsActionHandler().getPersistActions());
    }
}
