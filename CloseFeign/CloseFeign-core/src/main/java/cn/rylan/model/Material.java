package cn.rylan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    private Long id;

    private String name;

    private String icon;

    private String category;

    private String description;
}
