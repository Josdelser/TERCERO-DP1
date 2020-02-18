
package acme.features.employer.duty;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.jobs.Descriptor;
import acme.entities.jobs.Duty;
import acme.entities.roles.Employer;
import acme.entities.spam.Spam;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractCreateService;

@Service
public class EmployerDutyCreateService implements AbstractCreateService<Employer, Duty> {

	@Autowired
	EmployerDutyRepository repository;


	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);

	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "description", "percentage");

	}

	@Override
	public Duty instantiate(final Request<Duty> request) {
		assert request != null;

		return new Duty();

	}

	@Override
	public void validate(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		if (!errors.hasErrors("title")) {
			Spam spam = this.repository.findAllSpam().stream().collect(Collectors.toList()).get(0);
			Stream<String> spamWords = Stream.of(spam.getSpamWords().split(","));
			String title = (String) request.getModel().getAttribute("title");
			Double spamWordsTitle = (double) spamWords.filter(x -> title.toLowerCase().contains(x)).count();
			errors.state(request, spamWordsTitle < spam.getUmbral(), "title", "employer.duty.error.spam");
		}

		if (!errors.hasErrors("description")) {
			Spam spam = this.repository.findAllSpam().stream().collect(Collectors.toList()).get(0);
			Stream<String> spamWords = Stream.of(spam.getSpamWords().split(","));
			String title = (String) request.getModel().getAttribute("description");
			Double spamWordsTitle = (double) spamWords.filter(x -> title.toLowerCase().contains(x)).count();
			errors.state(request, spamWordsTitle < spam.getUmbral(), "description", "employer.duty.error.spam");
		}

		if (!errors.hasErrors("percentage")) {
			Spam spam = this.repository.findAllSpam().stream().collect(Collectors.toList()).get(0);
			Stream<String> spamWords = Stream.of(spam.getSpamWords().split(","));
			String title = (String) request.getModel().getAttribute("percentage");
			Double spamWordsTitle = (double) spamWords.filter(x -> title.toLowerCase().contains(x)).count();
			errors.state(request, spamWordsTitle < spam.getUmbral(), "percentage", "employer.duty.error.spam");
		}

		if (!errors.hasErrors("percentage")) {
			Boolean workloadTotal = entity.getPercentage() <= 100.0;
			errors.state(request, workloadTotal, "percentage", "employer.duty.error.maxWorkload");
		}

		if (!errors.hasErrors("percentage")) {
			Boolean workloadTotal = entity.getPercentage() > 0.0;
			Double workload = 0.0;
			Collection<Duty> duties = this.repository.findAllDutiesByDescriptor(request.getModel().getInteger("descriptor_id2"));
			for (Duty d : duties) {
				workload += d.getPercentage();
			}
			Boolean notWorkload = workload <= 100.00;
			errors.state(request, notWorkload, "percentage", "employer.job.error.notWorkload");
			errors.state(request, workloadTotal, "percentage", "employer.duty.error.minWorkload");
		}

	}

	@Override
	public void create(final Request<Duty> request, final Duty entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
		Descriptor a = this.repository.findDescriptorById(request.getModel().getInteger("descriptor_id2"));
		Collection<Duty> duties = a.getDuties();

		duties.add(entity);

		a.setDuties(duties);

		this.repository.save(a);

	}

}
