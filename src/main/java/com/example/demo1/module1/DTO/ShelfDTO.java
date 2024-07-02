package com.example.demo1.module1.DTO;

import com.example.demo1.module1.modules.Detail;
import com.example.demo1.module1.modules.Shelf;

import java.util.List;
import java.util.stream.Collectors;


public class ShelfDTO {
    private long shelfId;
    private List<DetailDTO> cells;
    public ShelfDTO setShelfId(long shelfId) {
        this.shelfId = shelfId;
        return this;
    }
    public long getShelfId() {
        return  shelfId;
    }
    public void setDetails(List<DetailDTO> cells) {
        this.cells = cells;
    }
    public List<DetailDTO> getDetails(){return cells;}
    public static ShelfDTO toDTO(Shelf shelf) {
        ShelfDTO shelfDTO = new ShelfDTO();
        shelfDTO.setShelfId(shelf.getShelfId());
        List<DetailDTO> detailDTOList = shelf.getDetails().stream()
                .map(cell -> {
                    DetailDTO detailDTO = new DetailDTO();
                    detailDTO.setDetailName(cell.getDetailName());
                    detailDTO.setQuantity(cell.getQuantity());
                    detailDTO.setDetailId(cell.getDetailId());
                    return detailDTO;
                })
                .collect(Collectors.toList());
        shelfDTO.setDetails(detailDTOList);
        return shelfDTO;
    }
    public static Shelf toEntity(ShelfDTO shelfDTO) {
        Shelf shelf = new Shelf();
        shelf.setShelfId(shelfDTO.getShelfId());
        List<Detail> cellList = shelfDTO.getDetails().stream()
                .map(cellDTO -> {
                    Detail cell = new Detail();
                    cell.setDetailName(cellDTO.getDetailName());
                    cell.setQuantity(cellDTO.getQuantity());
                    cell.setDetailId(cellDTO.getDetailId());
                    return cell;
                })
                .collect(Collectors.toList());
        shelf.setDetails(cellList);
        return shelf;
    }
}

