package Utility;

import net.jcip.annotations.Immutable;

@Immutable
public class Proposal {
	String username;
	String proposalName;
	byte[] proposal;

	public Proposal(String username, String fileName, byte[] fileContent) {
		this.username = username;
		this.proposalName = fileName;
		this.proposal = fileContent;
	}

	public String getUsername() {
		return this.username;
	}

	public String getProposalName() {
		return this.proposalName;
	}

	public byte[] getProposal() {
		return this.proposal;
	}

}
