package kr.co.myproject.Util;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ModelUtil {
    public void SetError(Model model, String errorMessage)
    {
        model.addAttribute("error", true);
        model.addAttribute("errorMessage", errorMessage);
    }
}
