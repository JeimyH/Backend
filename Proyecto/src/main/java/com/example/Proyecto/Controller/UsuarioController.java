package com.example.Proyecto.Controller;

import com.example.Proyecto.DTO.LoginDTO;
import com.example.Proyecto.DTO.UsuarioEntradaDTO;
import com.example.Proyecto.DTO.UsuarioRespuestaDTO;
import com.example.Proyecto.Model.Usuario;
import com.example.Proyecto.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/Usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    public UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        // Verificar si la lista está vacía
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(usuarios, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/buscar/{idUsuario}")
    public ResponseEntity<Usuario> listarPorIdUsuario(@PathVariable long idUsuario){
        try {
            Optional<Usuario> usuarioOpt = usuarioService.listarPorIdUsuario(idUsuario);
            return usuarioOpt.map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario){
        try {
            Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable long idUsuario){
        try {
            usuarioService.eliminarUsuario(idUsuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/actualizar/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable long idUsuario, @RequestBody Usuario usuarioActualizado){
        try {
            Usuario usuario = usuarioService.actualizarUsuario(idUsuario, usuarioActualizado);
            return new ResponseEntity<>(usuario, HttpStatus.OK); // 200 OK
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    private UsuarioRespuestaDTO mapToResponse(Usuario usuario) {
        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();
        respuesta.setIdUsuario(usuario.getIdUsuario());
        respuesta.setCorreo(usuario.getCorreo());
        respuesta.setNombre(usuario.getNombre());
        respuesta.setFechaNacimiento(usuario.getFechaNacimiento());
        respuesta.setAltura(usuario.getAltura());
        respuesta.setPeso(usuario.getPeso());
        respuesta.setSexo(usuario.getSexo());
        respuesta.setObjetivosSalud(usuario.getObjetivosSalud());
        respuesta.setRestriccionesDieta(usuario.getRestriccionesDieta());
        return respuesta;
    }

    @GetMapping("/existeCorreo")
    public ResponseEntity<Boolean> existeCorreo(@RequestParam String correo) {
        boolean existe = usuarioService.usuarioRepository.existsByCorreo(correo);
        return new ResponseEntity<>(existe, HttpStatus.OK);
    }

    @GetMapping("/existeNombre")
    public ResponseEntity<Boolean> existeNombre(@RequestParam String nombre) {
        boolean existe = usuarioService.usuarioRepository.existsByNombre(nombre);
        return new ResponseEntity<>(existe, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioEntradaDTO entradaDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(entradaDTO);
            UsuarioRespuestaDTO respuesta = mapToResponse(nuevoUsuario);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Devuelve un mensaje claro al frontend
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error inesperado al registrar usuario");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioRespuestaDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            Usuario usuario = usuarioService.autenticar(loginDTO.getCorreo(), loginDTO.getContrasena());

            if (usuario == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

            UsuarioRespuestaDTO response = new UsuarioRespuestaDTO(usuario);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // <-- Esto es clave para ver el error real
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
