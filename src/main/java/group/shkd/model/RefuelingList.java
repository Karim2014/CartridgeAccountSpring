package group.shkd.model;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefuelingList {
    private int id = -1;
    private String num = "";
    Set<Cartridge> cartridges = new LinkedHashSet<>();

    @Override
    public String toString() {
        return this.num;
    }
}
