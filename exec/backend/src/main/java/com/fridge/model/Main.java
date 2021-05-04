package com.fridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "main")
public class Main {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private Recipe recipe;

	@ManyToOne
	private Ingredientsdetail ingredientsdetail;

	public int getId() {
		return id;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public Ingredientsdetail getIngredientsdetail() {
		return ingredientsdetail;
	}

	@Override
	public String toString() {
		return "Main [id=" + id + ", recipe=" + recipe + ", ingredientsdetail=" + ingredientsdetail + "]";
	}

}
