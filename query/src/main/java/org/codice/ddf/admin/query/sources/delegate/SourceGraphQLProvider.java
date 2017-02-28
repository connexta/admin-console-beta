package org.codice.ddf.admin.query.sources.delegate;

import static org.codice.ddf.admin.query.graphql.GraphQLCommons.fieldsToGraphQLFieldDefinition;
import static org.codice.ddf.admin.query.graphql.GraphQLCommons.handlerToGraphQLObject;

import java.util.Collection;
import java.util.HashMap;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.servlet.GraphQLMutationProvider;
import graphql.servlet.GraphQLQueryProvider;

public class SourceGraphQLProvider implements GraphQLQueryProvider, GraphQLMutationProvider {

    @Override
    public Collection<GraphQLFieldDefinition> getMutations() {
        return fieldsToGraphQLFieldDefinition(new SourceDelegateActionHandler().getPersistActions());
    }

    @Override
    public GraphQLObjectType getQuery() {
        return handlerToGraphQLObject(new SourceDelegateActionHandler());
    }

    @Override
    public Object context() {
        return new HashMap<>();
    }
}