package org.samir.openshift.selfservices.web;

import javax.validation.Valid;

import org.samir.openshift.selfservices.dto.Project;
import org.samir.openshift.selfservices.svc.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectsController {

	private final static Logger log = LoggerFactory.getLogger(ProjectsController.class);

	@Autowired
	private ProjectsService projectsService;

	@RequestMapping(value = { "/", "/projects" }, method = RequestMethod.GET)
	public String getProjects(Model model) {
		log.info("Get projects");
		model.addAttribute("projects", projectsService.getProjects());
		return "projects";
	}

	@RequestMapping(value = { "/new-project" }, method = RequestMethod.GET)
	public String newProjectForm(Model model) {
		log.info("Create project form");
		model.addAttribute("project", new Project());
		return "new-project";
	}

	@RequestMapping(value = { "/new-project" }, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("project") Project project, BindingResult result, Model model) {
		log.info("Create project : {}", project);
		if (result.hasErrors()) {
			log.warn("Project validation erros: {}", result.getAllErrors());
			model.addAttribute("errors", result.getAllErrors());
			return "new-project";
		}

		projectsService.createProject(project);
		return getProjects(model);
	}

	@RequestMapping(value = { "/delete-project" }, method = RequestMethod.POST)
	public String deleteProject(@RequestParam("projectName") String projectName, Model model) {
		log.info("Delete project: {}", projectName);
		projectsService.deleteProject(projectName);
		return getProjects(model);
	}
}
