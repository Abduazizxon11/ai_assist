package org.example.model;

import lombok.*;
import org.example.Enum.BotState;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private long chatId;
    private BotState state;
    private List<String> history ;
}
