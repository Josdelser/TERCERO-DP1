
package acme.features.worker.job;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.applications.Applications;
import acme.entities.jobs.Job;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface WorkerJobRepository extends AbstractRepository {

	@Query("select a from Job a where a.id = ?1")
	Job findOneJobById(int id);

	@Query("select a from Job a where a.deadline>CURRENT_TIMESTAMP and a.finalMode = true and not exists (select b from Applications b where b.job.id = a.id)")
	Collection<Job> findManyJobsActive();

	@Query("select a from Applications a where a.worker.id=?1")
	Collection<Applications> findManyApplicationsByWorkerId(int id);
}
