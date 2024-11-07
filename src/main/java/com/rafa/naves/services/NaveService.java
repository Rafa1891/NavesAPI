package com.rafa.naves.services;





import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rafa.naves.model.Nave;
import com.rafa.naves.repository.NaveRepository;


import jakarta.persistence.EntityNotFoundException;

import java.util.List;


@Service
public class NaveService {

	
    private final NaveRepository naveRepository;

    
    public NaveService(NaveRepository NaveRepository) {
        this.naveRepository = NaveRepository;
    }

    // Obtiene todas las naves con paginación
    @Cacheable(value = "naves", key = "#pageable.pageNumber + '-' + #pageable.pageSize") // Caché por página
    public Page<Nave> getAllNaves(Pageable pageable) {
        return naveRepository.findAll(pageable);
    }

    // Obtiene una nave espacial por ID
    @Cacheable(value = "naves", key = "#id") // Caché por ID de nave
    public Nave getNaveById(Long id) {
        return naveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado la nave con id: " + id));
    }

    // Busca naves espaciales que contienen un nombre específico
    @Cacheable(value = "naves", key = "#nombre") // Caché por nombre de nave
    public List<Nave> searchByName(String nombre) {
        return naveRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Crea una nueva nave espacial
    public Nave createNave(Nave nave) {
        return naveRepository.save(nave);
    }

    // Actualiza una nave espacial existente
    public Nave updateNave(Long id, Nave datosNave) {
        Nave nave = naveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado la nave con id: " + id));
        
        nave.setNombre(datosNave.getNombre());
        nave.setTipo(datosNave.getTipo());
        nave.setSerie(datosNave.getSerie());
        nave.setPelicula(datosNave.getPelicula());
        
        return naveRepository.save(nave);
    }

    // Elimina una nave espacial por ID
    public void deleteNave(Long id) {
        Nave nave = naveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado la nave con id: " + id));
        
        naveRepository.delete(nave);
    }
}

