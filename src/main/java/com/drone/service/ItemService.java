package com.drone.service;

import com.drone.dto.ItemDto;
import com.drone.entity.Item;
import com.drone.error.ApplicationFailed;
import com.drone.repository.DroneModelRepository;
import com.drone.repository.ItemRepository;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    @Autowired
    private ImageService imageService;

    public Either<ApplicationFailed, ItemDto> registerItem(String name, String code, Long weight, MultipartFile image) {
        Either<ApplicationFailed, Boolean> validateItem = validateItem(name, code, weight);
        if (validateItem.isLeft())
            return Either.left(validateItem.getLeft());

        Either<ApplicationFailed, String> validateImageAndGetExtension = imageService.validateImageAndGetExtension(image);
        if (validateImageAndGetExtension.isLeft())
            return Either.left(validateImageAndGetExtension.getLeft());

        Item item = new Item();
        item.setName(name);
        item.setCode(code);
        item.setWeight(weight);
        Item savedItem = itemRepository.save(item);

        String imageUrl = imageService.uploadAndGetUrl(image, validateImageAndGetExtension.get(), savedItem.getId());
        savedItem.setImageUrl(imageUrl);
        return Either.right(itemRepository.save(savedItem).toItemDto());
    }

    private Either<ApplicationFailed, Boolean> validateItem(String name, String code, Long weight) {

        String nameFieldName = "name";
        String codeFieldName = "code";
        String weightFieldName = "weight";

        if (name == null) return Either.left(new ApplicationFailed.Required(nameFieldName));
        if (code == null) return Either.left(new ApplicationFailed.Required(codeFieldName));
        if (weight == null) return Either.left(new ApplicationFailed.Required(weightFieldName));

        if (!Pattern.matches("^[\\w\\d_-]+$", name))
            return Either.left(new ApplicationFailed.PatternNotMatch(nameFieldName, "Allowed only letters, numbers, ‘-‘, ‘_’"));

        if (!Pattern.matches("^[A-Z\\d_]+$", code))
            return Either.left(new ApplicationFailed.PatternNotMatch(codeFieldName, "Allowed only upper case letters, underscore and numbers"));

        Item itemByNameOrCode = itemRepository.findByNameOrCode(name, code);
        if (itemByNameOrCode != null && itemByNameOrCode.getName().equals(name))
            return Either.left(new ApplicationFailed.AlreadyExist(nameFieldName));
        if (itemByNameOrCode != null && itemByNameOrCode.getCode().equals(code))
            return Either.left(new ApplicationFailed.AlreadyExist(codeFieldName));

        Long maxMaxWeightLimit = droneModelRepository.findMaxMaxWeightLimit();
        if (weight > maxMaxWeightLimit)
            return Either.left(new ApplicationFailed.MaxLength(weightFieldName, maxMaxWeightLimit));

        return Either.right(true);
    }

    public List<ItemDto> findAll() {
        return itemRepository.findAll().stream().map(Item::toItemDto).toList();
    }

    public ItemDto findById(Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item != null) return item.toItemDto();
        else return null;
    }

}
