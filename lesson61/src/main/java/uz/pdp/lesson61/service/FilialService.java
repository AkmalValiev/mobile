package uz.pdp.lesson61.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson61.entity.Address;
import uz.pdp.lesson61.entity.Filial;
import uz.pdp.lesson61.payload.ApiResponse;
import uz.pdp.lesson61.payload.FilialDto;
import uz.pdp.lesson61.payload.FilialDtoUpdate;
import uz.pdp.lesson61.repository.AddressRepository;
import uz.pdp.lesson61.repository.FilialRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FilialService {
    @Autowired
    FilialRepository filialRepository;
    @Autowired
    AddressRepository addressRepository;
    public List<Filial> getFilials() {
        return filialRepository.findAll();
    }

    public Filial getFilial(Integer id) {
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        if (optionalFilial.isPresent())
            return optionalFilial.get();
        return null;
    }

    public ApiResponse addFilial(FilialDto filialDto) {
        boolean existsByName = filialRepository.existsByName(filialDto.getName());
        if (existsByName)
            return new ApiResponse("Bunday nomli filial mavjud!", false);

        Address address = new Address();
        address.setCity(filialDto.getCity());
        address.setStreet(filialDto.getStreet());
        Address saveAddress = addressRepository.save(address);

        Filial filial = new Filial();
        filial.setName(filialDto.getName());
        filial.setAddress(saveAddress);
        filialRepository.save(filial);
        return new ApiResponse("Filial qo'shildi!", true);

    }

    public ApiResponse editFilial(Integer id, FilialDtoUpdate filialDtoUpdate) {
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        if (!optionalFilial.isPresent())
            return new ApiResponse("Filial topilmadi!", false);
        Filial filial = optionalFilial.get();

        Optional<Address> optionalAddress = addressRepository.findById(filialDtoUpdate.getId());
        if (!optionalAddress.isPresent())
            return new ApiResponse("Address topilmadi!", false);
        Address address = optionalAddress.get();

        boolean existsByIdAndNameNot = filialRepository.existsByIdAndNameNot(id, filialDtoUpdate.getName());
        if (existsByIdAndNameNot)
            return new ApiResponse("Bunday nomli filial mavjud!", false);
        filial.setAddress(address);
        filial.setName(filialDtoUpdate.getName());
        filialRepository.save(filial);
        return new ApiResponse("Filial taxrirlandi!", true);
    }

    public ApiResponse deleteFilial(Integer id) {
        try {
            filialRepository.deleteById(id);
            return new ApiResponse("Filial o'chirildi!", true);
        }catch (Exception e){
            return new ApiResponse("Xatolik!", false);
        }
    }
}
