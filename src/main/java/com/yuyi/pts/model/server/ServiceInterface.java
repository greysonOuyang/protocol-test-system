package com.yuyi.pts.model.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ServiceInterface {
    private String name;
    private List<Param> input;
    private List<Param> output;

}
