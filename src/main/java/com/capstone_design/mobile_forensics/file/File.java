package com.capstone_design.mobile_forensics.file;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter @ToString
public class File {

    @Id
    Long fileId;


}
