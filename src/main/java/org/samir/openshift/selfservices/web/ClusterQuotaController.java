package org.samir.openshift.selfservices.web;

import javax.validation.Valid;

import org.samir.openshift.selfservices.dto.ClusterResourceQuotaDto;
import org.samir.openshift.selfservices.dto.ProjectDto;
import org.samir.openshift.selfservices.svc.ClusterQuotasServices;
import org.samir.openshift.selfservices.svc.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClusterQuotaController {

	private final static Logger log = LoggerFactory.getLogger(ClusterQuotaController.class);

	@Autowired
	private ClusterQuotasServices clusterQuotasServices;

	/* =====================================================
	 *       GET
	 * =====================================================*/
	// Get all quotas
	@RequestMapping(value = {"/quotas" }, method = RequestMethod.GET)
	public String getQuotas(Model model) {
		log.info("Get quotas");
		model.addAttribute("quotas", clusterQuotasServices.getClusterResourceQuotas());
		return "quotas/list";
	}
	
	/*@RequestMapping(value = "/quotas/{id}", method = RequestMethod.GET)
	public String getProject(@PathVariable("id") int id, Model model) {

		return "projects/view";
	}*/

	
	/* =====================================================
	 *       CREATE FORM
	 * =====================================================*/
	
	@RequestMapping(value = { "/quotas/add" }, method = RequestMethod.GET)
	public String newQuotaForm(Model model) {
		log.info("Create quota form");
		model.addAttribute("quota", new ClusterResourceQuotaDto());
		return "quotas/edit";
	}

	/* =====================================================
	 *       UPDATE FORM
	 * =====================================================*/
	/*@RequestMapping(value = "/projects/{id}/update", method = RequestMethod.GET)
	public String updateProject(@PathVariable("id") int id, Model model) {

		return "projects/edit";

	}*/
	
	/* =====================================================
	 *       SAVE CHANGES
	 * =====================================================*/
	@RequestMapping(value = { "/quotas" }, method = RequestMethod.POST)
	public String createQuota(@Valid @ModelAttribute("quota") ClusterResourceQuotaDto quota, BindingResult result, 
			Model model, final RedirectAttributes redirectAttributes) {
		log.info("Create quota : {}", quota);
		if (result.hasErrors()) {
			log.warn("Quota validation erros: {}", result.getAllErrors());
			return "quotas/edit";
		} else {
			try {
				ClusterResourceQuotaDto ocQuota = clusterQuotasServices.getClusterResourceQuota(quota.getName());
				
				if(ocQuota != null) {
					model.addAttribute("css", "danger");
					model.addAttribute("msg", "Quota " + quota.getName() + " already exist!");
					return "quotas/edit";
				}
				
				clusterQuotasServices.createQuota(quota);
				redirectAttributes.addFlashAttribute("css", "success");
				redirectAttributes.addFlashAttribute("msg", "Quota created successfully!");
			} catch (Exception e) {
				model.addAttribute("css", "danger");
				model.addAttribute("msg", e.getMessage());
				return "quotas/edit";
			}
		}
		return "redirect:/quotas";
	}
	
	/* =====================================================
	 *       DELETE
	 * =====================================================*/
	/*@RequestMapping(value = "/projects/{name}/delete", method = RequestMethod.POST)
	public String deleteProject(@PathVariable("name") String name, final RedirectAttributes redirectAttributes) {

		log.info("Delete project: {}", name);
		projectsService.deleteProject(name);
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Project is deleted!");
		
		return "redirect:/projects";

	}*/
}
