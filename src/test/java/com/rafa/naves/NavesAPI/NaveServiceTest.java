package com.rafa.naves.NavesAPI;

import com.rafa.naves.model.Nave;
import com.rafa.naves.repository.NaveRepository;
import com.rafa.naves.services.NaveService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NaveServiceTest {

    @Mock
    private NaveRepository naveRepository;

    @InjectMocks
    private NaveService naveService;

    private Nave nave;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nave = new Nave();
        nave.setId(1L);
        nave.setNombre("Halcón Milenario");
        nave.setTipo("Carguero Ligero");
        nave.setSerie("Varias");
        nave.setPelicula("Star Wars");
    }

    @Test
    void testGetAllNaves() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Nave> page = new PageImpl<>(Arrays.asList(nave));

        when(naveRepository.findAll(pageable)).thenReturn(page);

        Page<Nave> result = naveService.getAllNaves(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Halcón Milenario", result.getContent().get(0).getNombre());
        verify(naveRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetNaveById() {
        when(naveRepository.findById(1L)).thenReturn(Optional.of(nave));

        Nave result = naveService.getNaveById(1L);

        assertNotNull(result);
        assertEquals("Halcón Milenario", result.getNombre());
        verify(naveRepository, times(1)).findById(1L);
    }

    @Test
    void testGetNaveByIdNotFound() {
        when(naveRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> naveService.getNaveById(1L));

        assertEquals("No se ha encontrado la nave con id: 1", exception.getMessage());
        verify(naveRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchByName() {
        when(naveRepository.findByNombreContainingIgnoreCase("Halcón")).thenReturn(Arrays.asList(nave));

        List<Nave> result = naveService.searchByName("Halcón");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Halcón Milenario", result.get(0).getNombre());
        verify(naveRepository, times(1)).findByNombreContainingIgnoreCase("Halcón");
    }

    @Test
    void testSearchByNameNoResults() {
        when(naveRepository.findByNombreContainingIgnoreCase("Inexistente")).thenReturn(Arrays.asList());

        List<Nave> result = naveService.searchByName("Inexistente");

        assertTrue(result.isEmpty());
        verify(naveRepository, times(1)).findByNombreContainingIgnoreCase("Inexistente");
    }

    @Test
    void testCreateNave() {
        when(naveRepository.save(nave)).thenReturn(nave);

        Nave result = naveService.createNave(nave);

        assertNotNull(result);
        assertEquals("Halcón Milenario", result.getNombre());
        assertEquals("Carguero Ligero", result.getTipo());
        verify(naveRepository, times(1)).save(nave);
    }

    @Test
    void testUpdateNave() {
        Nave datosNave = new Nave();
        datosNave.setNombre("X-Wing");
        datosNave.setTipo("Caza Estelar");
        datosNave.setSerie("Andor");
        datosNave.setPelicula("Star Wars");

        when(naveRepository.findById(1L)).thenReturn(Optional.of(nave));
        when(naveRepository.save(nave)).thenReturn(nave);

        Nave result = naveService.updateNave(1L, datosNave);

        assertNotNull(result);
        assertEquals("X-Wing", result.getNombre());
        assertEquals("Caza Estelar", result.getTipo());
        verify(naveRepository, times(1)).findById(1L);
        verify(naveRepository, times(1)).save(nave);
    }

    @Test
    void testUpdateNaveNotFound() {
        Nave datosNave = new Nave();
        datosNave.setNombre("X-Wing");

        when(naveRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> naveService.updateNave(1L, datosNave));

        assertEquals("No se ha encontrado la nave con id: 1", exception.getMessage());
        verify(naveRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteNave() {
        when(naveRepository.findById(1L)).thenReturn(Optional.of(nave));
        doNothing().when(naveRepository).delete(nave);

        naveService.deleteNave(1L);

        verify(naveRepository, times(1)).findById(1L);
        verify(naveRepository, times(1)).delete(nave);
    }

    @Test
    void testDeleteNaveNotFound() {
        when(naveRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> naveService.deleteNave(1L));

        assertEquals("No se ha encontrado la nave con id: 1", exception.getMessage());
        verify(naveRepository, times(1)).findById(1L);
    }
}

