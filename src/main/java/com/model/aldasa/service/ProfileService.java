package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;


import com.model.aldasa.entity.Profile;

public interface ProfileService {
 
   Optional<Profile> findBy(Integer id);
   Profile save(Profile profile);
   void delete(Profile profile);
   List<Profile> findByStatus(boolean status);
   Profile findByName (String name);
   Profile findByNameException(String name, int idProfile);
}
