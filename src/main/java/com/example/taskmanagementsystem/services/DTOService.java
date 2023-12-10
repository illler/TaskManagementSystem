package com.example.taskmanagementsystem.services;


import com.example.taskmanagementsystem.DTO.UserDTO;
import com.example.taskmanagementsystem.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DTOService {

    private final ModelMapper modelMapper;

    public UserDTO convertToUserDTO(User person) {
        return modelMapper.map(person, UserDTO.class);
    }

    public User convertToUser(UserDTO personDTO) {
        return modelMapper.map(personDTO, User.class);
    }
}
