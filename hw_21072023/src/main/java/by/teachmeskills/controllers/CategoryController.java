package by.teachmeskills.controllers;

import by.teachmeskills.constants.RequestAttributesNames;
import by.teachmeskills.services.ProductService;
import by.teachmeskills.services.implementation.ProductServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("categories")
public class CategoryController {
    private static final ProductService service = new ProductServiceImpl();

    @GetMapping("{name}")
    public ModelAndView openCategory(@PathVariable String name) {
        ModelAndView modelAndView = service.getCategoryProducts(name);
        modelAndView.addObject(RequestAttributesNames.CATEGORY_NAME, name);
        return modelAndView;
    }
}
