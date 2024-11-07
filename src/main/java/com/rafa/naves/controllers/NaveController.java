package com.rafa.naves.controllers;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafa.naves.model.Nave;
import com.rafa.naves.services.NaveService;

@RestController
@RequestMapping("/naves")
public class NaveController {

    private final NaveService naveService;

    public NaveController(NaveService naveService) {
        this.naveService = naveService;
    }

    @GetMapping
    public Page<Nave> todasLasNaves(Pageable pageable) {
        return naveService.getAllNaves(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nave> buscarNavePorId(@PathVariable Long id) {
        return ResponseEntity.ok(naveService.getNaveById(id));
    }

    @GetMapping("/search")
    public List<Nave> buscarNavesPorNombre(@RequestParam String nombre) {
        return naveService.searchByName(nombre);
    }

    @PostMapping
    public ResponseEntity<Nave> crearNave(@RequestBody Nave nave) {
        return ResponseEntity.status(HttpStatus.CREATED).body(naveService.createNave(nave));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nave> actualizarNave(@PathVariable Long id, @RequestBody Nave nave) {
        return ResponseEntity.ok(naveService.updateNave(id, nave));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNave(@PathVariable Long id) {
        naveService.deleteNave(id);
        return ResponseEntity.noContent().build();
    }
}

