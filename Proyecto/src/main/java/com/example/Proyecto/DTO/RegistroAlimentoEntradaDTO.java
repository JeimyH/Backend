package com.example.Proyecto.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAlimentoEntradaDTO {
    private Long idUsuario;
    private Long idAlimento;
    private Float tamanoPorcion;
    private String unidadMedida;
    private String momentoDelDia;
}

