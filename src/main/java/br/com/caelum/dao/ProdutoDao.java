package br.com.caelum.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.caelum.model.Loja;
import br.com.caelum.model.Produto;

@Repository
public class ProdutoDao {

	@PersistenceContext
	private EntityManager em;

	public List<Produto> getProdutos() {
		return em.createQuery("from Produto", Produto.class).getResultList();
	}

	public Produto getProduto(Integer id) {
		Produto produto = em.find(Produto.class, id);
		return produto;
	}

	public List<Produto> getProdutos(String nome, Integer categoriaId, Integer lojaId) {
		
		String jpql = "select p from Produto p ";
		
		if(categoriaId != null)
			jpql += "join fetch p.categorias c where c.id = :pCategoriaId and ";
		else
			jpql += " where ";
		if(lojaId != null)
			jpql += "p.loja.id = :pLojaId and ";
		if(!nome.isEmpty())
			jpql += "p.nome like :pProdutoNome and ";
		
		jpql += " 1 = 1";
		
		TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
		
		if(categoriaId != null)
			query.setParameter("pCategoriaId", categoriaId);
		if(lojaId != null)
			query.setParameter("pLojaId", lojaId);
		if(!nome.isEmpty())
			query.setParameter("pProdutoNome", nome);
		
		return query.getResultList();

	}

	public void insere(Produto produto) {
		if (produto.getId() == null)
			em.persist(produto);
		else
			em.merge(produto);
	}

}
