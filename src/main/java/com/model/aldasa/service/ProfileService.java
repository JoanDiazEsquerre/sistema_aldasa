package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;


import com.model.aldasa.entity.Profile;

public interface ProfileService {
 
   Optional<Profile> findBy(Integer id);
   Profile save(Profile client);
   void delete(Profile client);
   List<Profile> findByStatus(boolean status);
   
}
