package kr.lifesemantics.canofymd.moduleapi.global.config.security;

import kr.lifesemantics.canofymd.moduleapi.domain.patient.repository.PatientRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.repository.StaffRepository;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import kr.lifesemantics.canofymd.modulecore.enums.AuthType;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityDetailsService implements UserDetailsService {

	StaffRepository staffRepository;
	PatientRepository patientRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String authTypeAndId) throws UsernameNotFoundException {

		String authType = authTypeAndId.split("/")[0];
		String id = authTypeAndId.split("/")[1];

		// TODO refac + exception
		SecurityUserInfo securityUserInfo = null;
		List<SimpleGrantedAuthority> authorities = null;

		if(AuthType.valueOf(authType) == AuthType.STAFF) {
			Staff staff = staffRepository.findByStaffId(id);
			securityUserInfo = SecurityUserInfo.madeFromStaff(staff, staff.getUserType() == UserType.ADMIN ? null : staff.getCategories().stream().map(i -> i.getCategory()).toList());
			authorities = staff.getUserType().getRoles().stream().map(SimpleGrantedAuthority::new).toList();
		} else if (AuthType.valueOf(authType) == AuthType.PATIENT) {
			Patient patient = patientRepository.findByPatientId(id);
			securityUserInfo = SecurityUserInfo.madeFromPatient(patient, List.of(patient.getCategory().getCategory()));
		}

		return new SecurityUserDetails(securityUserInfo, authorities);
	}

}
