package br.com.andorm.test;

import static br.com.andorm.reflection.Reflactor.in;
import static br.com.andorm.reflection.Reflactor.invoke;
import static com.jonatasdaniel.criteria.Restriction.like;
import static com.jonatasdaniel.criteria.Restriction.match;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.test.AndroidTestCase;
import br.com.andorm.persistence.AndroidQueryBuilder;
import br.com.andorm.persistence.EntityCache;
import br.com.andorm.persistence.property.PrimaryKeyProperty;
import br.com.andorm.persistence.property.Property;
import br.com.andorm.test.entity.BasicClient;

import com.jonatasdaniel.criteria.Criteria;

public class QueryBuilderTest extends AndroidTestCase {
	
	public void testShouldHaveOneLikeClause() {
		Criteria criteria = new Criteria(BasicClient.class);
		criteria.where(like("nome", "joaozinho"));
		
		AndroidQueryBuilder queryBuilder = new AndroidQueryBuilder(criteria, entityCache());
		queryBuilder.build();
		
		assertEquals("nome LIKE ?", queryBuilder.whereClause());
		assertTrue(queryBuilder.whereArgs().length == 1);
		assertEquals("joaozinho", queryBuilder.whereArgs()[0]);
		assertNull(queryBuilder.groupBy());
		assertNull(queryBuilder.having());
		assertNull(queryBuilder.orderBy());
	}
	
	public void testShouldHaveOneMatchClause() {
		Criteria criteria = new Criteria(BasicClient.class);
		criteria.where(match("nome", "joaozinho"));
		
		AndroidQueryBuilder queryBuilder = new AndroidQueryBuilder(criteria, entityCache());
		queryBuilder.build();
		
		assertEquals("nome = ?", queryBuilder.whereClause());
		assertTrue(queryBuilder.whereArgs().length == 1);
		assertEquals("joaozinho", queryBuilder.whereArgs()[0]);
		assertNull(queryBuilder.groupBy());
		assertNull(queryBuilder.having());
		assertNull(queryBuilder.orderBy());
	}
	
	private EntityCache entityCache() {
		EntityCache cache = new EntityCache(BasicClient.class, "basic_client");
		addIdProperty(cache);
		addNomeProperty(cache);
		addEnderecoProperty(cache);
		
		return cache;
	}
	
	private void addIdProperty(EntityCache cache) {
		Field field = in(BasicClient.class).returnField("id");
		Method setMethod = in(BasicClient.class).returnSetMethodOf(field);
		Method getMethod = in(BasicClient.class).returnGetMethodOf(field);
		Property prop = new PrimaryKeyProperty("id", field, getMethod, setMethod, true);
		Method setPkMethod = in(EntityCache.class).returnMethod("setPk", PrimaryKeyProperty.class);
		setPkMethod.setAccessible(true);
		invoke(cache, setPkMethod).withParams(prop);
	}
	
	private void addNomeProperty(EntityCache cache) {
		Field field = in(BasicClient.class).returnField("nome");
		Method setMethod = in(BasicClient.class).returnSetMethodOf(field);
		Method getMethod = in(BasicClient.class).returnGetMethodOf(field);
		Property prop = new Property("nome", field, getMethod, setMethod);
		Method addPropertyMethod = in(EntityCache.class).returnMethod("add", Property.class);
		addPropertyMethod.setAccessible(true);
		invoke(cache, addPropertyMethod).withParams(prop);
	}
	
	private void addEnderecoProperty(EntityCache cache) {
		Field field = in(BasicClient.class).returnField("endereco");
		Method setMethod = in(BasicClient.class).returnSetMethodOf(field);
		Method getMethod = in(BasicClient.class).returnGetMethodOf(field);
		Property prop = new Property("endereco", field, getMethod, setMethod);
		Method addPropertyMethod = in(EntityCache.class).returnMethod("add", Property.class);
		addPropertyMethod.setAccessible(true);
		invoke(cache, addPropertyMethod).withParams(prop);
	}
	
}