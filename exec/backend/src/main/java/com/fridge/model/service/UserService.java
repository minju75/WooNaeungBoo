package com.fridge.model.service;

import com.fridge.model.User;

public interface UserService  {
	public User login(User user) throws Exception;
	
	public void join(User user) throws Exception;
}