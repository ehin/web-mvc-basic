package spring.servlet.demo;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getAddress(){
        return "Tokyo";
    }
}
