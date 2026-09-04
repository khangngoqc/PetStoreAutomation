package api.utilities;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.javafaker.Faker;

import api.payload.Category;
import api.payload.Pet;
import api.payload.Tag;

public class PetTestDataFactory {
	
	private static final Faker faker = new Faker();
	private static final Random random = new Random();
	private static final List<String> STATUSES = List.of("available", "pending", "sold");
	
	
	public static Pet randomPet() {
		
		Pet pet = new Pet();
		pet.setId(faker.number().numberBetween(1, 100));
		pet.setCategory(randomCategory());
		pet.setName(faker.funnyName().name());
		pet.setPhotoUrls(randomPhotoUrls());
		pet.setTags(randomTags());
		pet.setStatus(randomStatus());
		
		return pet;
	}
	
	private static Category randomCategory() {
		return new Category(
				faker.number().numberBetween(1, 100),
				faker.animal().name() + "category"
				);
	}
	
	private static List<String> randomPhotoUrls(){
		int count = random.nextInt(3) + 1; //1-3 urls
		return IntStream.range(0, count)
				.mapToObj(i -> faker.internet().image())
				.collect(Collectors.toList());
	}
	
	private static List<Tag> randomTags(){
		int count = random.nextInt(3) + 1; //1-3 tags
		return IntStream.range(0, count)
				.mapToObj(i -> new Tag(
						faker.number().numberBetween(1, 1000),
						faker.lorem().word()
				))
				.collect(Collectors.toList());
	}
	
	private static String randomStatus() {
		return STATUSES.get(random.nextInt(STATUSES.size()));
	}

}
