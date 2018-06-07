package com.prista.netbanking.web.controller;

import com.prista.netbanking.web.dto.address.AddressDTO;
import com.prista.netbanking.web.dto.address.CityDTO;
import com.prista.netbanking.web.dto.address.CountryDTO;
import com.prista.netbanking.web.dto.address.RegionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/ajax-samples")
public class AjaxSamplesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AjaxSamplesController.class);

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public ResponseEntity<List<CountryDTO>> getCountries() {
        final List<CountryDTO> countries = new ArrayList<CountryDTO>();
        countries.add(new CountryDTO(1, "Belarus"));
        countries.add(new CountryDTO(2, "Russia"));
        countries.add(new CountryDTO(3, "France"));
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public ResponseEntity<List<RegionDTO>> getRegions(
            @RequestParam(name = "countryId", required = true) final Integer countryId) {
        final List<RegionDTO> regions = new ArrayList<>();
        if (countryId.equals(1)) {
            regions.add(new RegionDTO(1, "Гродненская область"));
            regions.add(new RegionDTO(2, "Минская область"));
            regions.add(new RegionDTO(3, "Витебская область"));
            regions.add(new RegionDTO(4, "Гомельская область"));
            regions.add(new RegionDTO(5, "Могилевская область"));
            regions.add(new RegionDTO(6, "Брестская область"));
        } else if (countryId.equals(2)) {
            regions.add(new RegionDTO(7, "russian region 1"));
            regions.add(new RegionDTO(8, "russian region 2"));
            regions.add(new RegionDTO(9, "russian region 3"));
        } else if (countryId.equals(3)) {
            regions.add(new RegionDTO(10, "france region 1"));
            regions.add(new RegionDTO(11, "france region 1"));
        }
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public ResponseEntity<List<CityDTO>> getCities(
            @RequestParam(name = "regionId", required = true) final Integer regionId) {
        final List<CityDTO> cities = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            final int id = (10 * regionId) + i;
            cities.add(new CityDTO(id, "город_" + id));
        }

        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object save(@Valid @ModelAttribute("addressForm") final AddressDTO formModel, final BindingResult result) {
        LOGGER.info("FORM RECEIVED: {}", formModel);
        return "redirect:/ajax-samples";
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showPage() {
        return new ModelAndView("ajax-samples", "addressForm", new AddressDTO());
    }

}
