package org.samir.openshift.selfservices.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.samir.openshift.selfservices.dto.ClusterResourceQuotaDto;
import org.samir.openshift.selfservices.map.ClusterQuotaMapper;
import org.samir.openshift.selfservices.svc.ClusterQuotasService;
import org.samir.openshift.selfservices.svc.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.fabric8.openshift.api.model.ClusterResourceQuota;

@Controller
public class ClusterQuotaController {

	private final static Logger log = LoggerFactory.getLogger(ClusterQuotaController.class);

	@Autowired
	private ClusterQuotaMapper clusterQuotaMapper;
	
	@Autowired
	private ClusterQuotasService clusterQuotasServices;
	
	@Autowired
	private UsersService usersService;

	/* =====================================================
	 *       GET
	 * =====================================================*/
	// Get all quotas
	@RequestMapping(value = {"/quotas" }, method = RequestMethod.GET)
	public String getQuotas(Model model) {
		log.info("Get quotas");
		List<ClusterResourceQuota> quotas = clusterQuotasServices.getClusterResourceQuotas();
		List<ClusterResourceQuotaDto> quotaDtos = clusterQuotaMapper.map(quotas);
		model.addAttribute("quotas", quotaDtos);
		return "quotas/list";
	}
	
	/*@RequestMapping(value = "/quotas/{id}", method = RequestMethod.GET)
	public String getQuota(@PathVariable("id") int id, Model model) {

		return "quotas/view";
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
	@RequestMapping(value = "/quotas/{name}/update", method = RequestMethod.GET)
	public String updateQuota(@PathVariable("name") String name, Model model) {
		log.info("Edit quota form");
		ClusterResourceQuota ocQuota = clusterQuotasServices.getClusterResourceQuota(name);
		ClusterResourceQuotaDto quota = clusterQuotaMapper.map(ocQuota);
		model.addAttribute("quota", quota);
		return "quotas/edit";

	}
	
	/* =====================================================
	 *       SAVE CHANGES
	 * =====================================================*/
	@RequestMapping(value = { "/quotas" }, method = RequestMethod.POST)
	public String saveQuota(@Valid @ModelAttribute("quota") ClusterResourceQuotaDto quota, BindingResult result, 
			Model model, final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			log.warn("Quota validation erros: {}", result.getAllErrors());
			return "quotas/edit";
		} else {
			if (quota.getCreatedOn() == null) {
				return createQuota(quota, result, model, redirectAttributes);
			} else {
				return updateQuota(quota, result, model, redirectAttributes);
			}
		}
	}
	
	private String createQuota(ClusterResourceQuotaDto quota, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {

		log.info("Creating new quota : {}", quota);
		try {
			List<String> errors = new ArrayList<>();
			if (clusterQuotasServices.getClusterResourceQuota(quota.getName()) != null) {
				errors.add("Quota " + quota.getName() + " aleady exist!");
			}
			
			if(usersService.getUser(quota.getOwner()) == null) {
				errors.add("User " + quota.getOwner() + " doesn't exist!");
			}

			if (errors.size() > 0) {
				model.addAttribute("css", "danger");
				model.addAttribute("msg", StringUtils.collectionToDelimitedString(errors, "<br>"));
				return "quotas/edit";
			} else {
				ClusterResourceQuota ocQuota = clusterQuotaMapper.map(quota);
				clusterQuotasServices.createQuota(ocQuota);
				
				redirectAttributes.addFlashAttribute("css", "success");
				redirectAttributes.addFlashAttribute("msg", "Quota created successfully!");
				return "redirect:/quotas";
			}

		} catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("css", "danger");
			model.addAttribute("msg", e.getMessage());
			return "quotas/edit";
		}
	}
	
	private String updateQuota(ClusterResourceQuotaDto quota, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {

		log.info("Updating Quota : {}", quota);
		try {
			List<String> errors = new ArrayList<>();
			ClusterResourceQuota ocQuota = clusterQuotasServices.getClusterResourceQuota(quota.getName());
			if (ocQuota == null) {
				errors.add("Quota " + quota.getName() + " doesn't exist!");
			}
			
			if(usersService.getUser(quota.getOwner()) == null) {
				errors.add("User " + quota.getOwner() + " doesn't exist!");
			}

			if (errors.size() > 0) {
				model.addAttribute("css", "danger");
				model.addAttribute("msg", StringUtils.collectionToDelimitedString(errors, "<br>"));
				return "quotas/edit";
			} else {
				clusterQuotaMapper.map(quota, ocQuota);
				clusterQuotasServices.updateQuota(ocQuota);
				
				redirectAttributes.addFlashAttribute("css", "success");
				redirectAttributes.addFlashAttribute("msg", "Quota updated successfully!");
				return "redirect:/quotas";
			}

		} catch (Exception e) {
			e.printStackTrace();
						
			model.addAttribute("css", "danger");
			model.addAttribute("msg", e.getMessage());
			return "quotas/edit";
		}
	}
	
	/* =====================================================
	 *       DELETE
	 * =====================================================*/
	@RequestMapping(value = "/quotas/{name}/delete", method = RequestMethod.POST)
	public String deleteQuota(@PathVariable("name") String name, final RedirectAttributes redirectAttributes) {

		log.info("Delete quota: {}", name);
		try {
			clusterQuotasServices.deleteQuota(name);
	
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Quota is deleted!");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("css", "danger");
			redirectAttributes.addFlashAttribute("msg", e.getMessage());
		}

		return "redirect:/quotas";

	}
}
