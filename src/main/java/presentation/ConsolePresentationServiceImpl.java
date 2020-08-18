package presentation;

public class ConsolePresentationServiceImpl implements PresentationService {
	@Override
	public void present(String content) {
		System.out.println(content);
	}
}
