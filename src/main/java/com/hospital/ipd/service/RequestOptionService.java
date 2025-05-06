package com.hospital.ipd.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.ipd.model.PriorityEnum;
import com.hospital.ipd.model.RequestOption;
import com.hospital.ipd.repository.RequestOptionRepository;

@Service
public class RequestOptionService {

    @Autowired
    private RequestOptionRepository requestOptionRepository;
    

    /**
     * Fetch all RequestOptions, filtered by the given priorities and/or role IDs.
     * If either list is null (or empty), it is treated as “ALL” for that dimension.
     */
    public List<RequestOption> findFiltered(List<PriorityEnum> priorities, List<Integer> roleIds) {
        return requestOptionRepository.findAll().stream()
                .filter(opt -> {
                    // if no priority filter, accept all; otherwise only those in the list
                    return priorities == null
                            || priorities.isEmpty()
                            || priorities.contains(opt.getPriority());
                })
                .filter(opt -> {
                    // similarly for responsible role
                    Integer rid = opt.getResponsibleRole().getRoleId();
                    return roleIds == null
                            || roleIds.isEmpty()
                            || roleIds.contains(rid);
                })
                .collect(Collectors.toList());
    }

    /**
     * Find one RequestOption by its ID.
     */
    public Optional<RequestOption> findById(int id) {
        return requestOptionRepository.findById(id);
    }

    /**
     * Save or update a RequestOption.
     */
    public RequestOption save(RequestOption requestOption) {
        return requestOptionRepository.save(requestOption);
    }

    /**
     * Delete (if no tasks are attached—you should enforce that check in your controller or repository layer).
     */
    public void deleteById(int id) {
        requestOptionRepository.deleteById(id);
    }

    public RequestOption getRequestOptionById(int optionId) {
        // Fetch the request option by its ID from the repository
        Optional<RequestOption> option = requestOptionRepository.findById(optionId);
        // If it exists, return it, else return null or throw an exception (depends on your use case)
        return option.orElse(null);  // Or throw an exception if preferred
    }

    // create findAllOptions: findAll from RequestOptionRepository
    /**
 * Fetch all RequestOptions without any filtering.
 */
    public List<RequestOption> findAllOptions() {
        return requestOptionRepository.findAll();
    }
}