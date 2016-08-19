/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2016 The JavaParser Team.
 *
 * This file is part of JavaParser.
 * 
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License 
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
 
package com.github.javaparser;

import static com.github.javaparser.utils.Utils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.PrimitiveType.Primitive;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;

/**
 * This class helps to construct new nodes.
 * 
 * @author Júlio Vilmar Gesser
 */
public final class ASTHelper {

    private ASTHelper() {
        // nop
    }

    /**
     * Adds the given type declaration to the compilation unit. The list of
     * types will be initialized if it is <code>null</code>.
     * 
     * @param cu
     *            compilation unit
     * @param type
     *            type declaration
     * @deprecated use {@link CompilationUnit#addClass(String)} kind of methods
     */
    @Deprecated
    public static void addTypeDeclaration(CompilationUnit cu, TypeDeclaration<?> type) {
        List<TypeDeclaration<?>> types = cu.getTypes();
        if (isNullOrEmpty(types)) {
            types = new ArrayList<>();
            cu.setTypes(types);
        }
        types.add(type);
        type.setParentNode(cu);
    }

    /**
     * Creates a new {@link ReferenceType} for a class or interface.
     * 
     * @param name
     *            name of the class or interface
     * @param arrayCount
     *            number of arrays or <code>0</code> if is not a array.
     * @return instanceof {@link ReferenceType}
     */
    public static ReferenceType createReferenceType(String name, int arrayCount) {
        return new ReferenceType(new ClassOrInterfaceType(name), arrayCount);
    }

    /**
     * Creates a new {@link ReferenceType} for the given primitive type.
     * 
     * @param type
     *            primitive type
     * @param arrayCount
     *            number of arrays or <code>0</code> if is not a array.
     * @return instanceof {@link ReferenceType}
     */
    public static ReferenceType createReferenceType(PrimitiveType type, int arrayCount) {
        return new ReferenceType(type, arrayCount);
    }

    /**
     * Adds the given expression to the specified block. The list of statements
     * will be initialized if it is <code>null</code>.
     * 
     * @param block to have expression added to
     * @param expr to be added
     */
    public static void addStmt(BlockStmt block, Expression expr) {
        block.addStatement(new ExpressionStmt(expr));
    }

    /**
     * Adds the given declaration to the specified type. The list of members
     * will be initialized if it is <code>null</code>.
     * 
     * @param type
     *            type declaration
     * @param decl
     *            member declaration
     */
    public static void addMember(TypeDeclaration<?> type, BodyDeclaration<?> decl) {
        List<BodyDeclaration<?>> members = type.getMembers();
        if (isNullOrEmpty(members)) {
            members = new ArrayList<>();
            type.setMembers(members);
        }
        members.add(decl);
        decl.setParentNode(type);
    }

    public static <N extends Node> List<N> getNodesByType(Node container, Class<N> clazz) {
        List<N> nodes = new ArrayList<>();
        for (Node child : container.getChildrenNodes()) {
            if (clazz.isInstance(child)) {
                nodes.add(clazz.cast(child));
            }
            nodes.addAll(getNodesByType(child, clazz));
        }
        return nodes;
    }

}
