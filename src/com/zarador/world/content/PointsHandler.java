package com.zarador.world.content;

import com.zarador.GameSettings;
import com.zarador.world.entity.impl.player.Player;

public class PointsHandler {

	private Player p;

	public PointsHandler(Player p) {
		this.p = p;
	}

	public void reset() {
		dungTokens = commendations = (int) (loyaltyPoints = votingPoints = slayerPoints = pkPoints = 0);
		p.getPlayerKillingAttributes().setPlayerKillStreak(0);
		p.getPlayerKillingAttributes().setPlayerKills(0);
		p.getPlayerKillingAttributes().setPlayerDeaths(0);
		p.getDueling().arenaStats[0] = p.getDueling().arenaStats[1] = 0;
	}

	public PointsHandler refreshPanel() {
		/*
		p.getPacketSender().sendString(55079, "@red@Prestige Points: @gre@" + Misc.format(prestigePoints));
		p.getPacketSender().sendString(55080, "@red@Commendations: @gre@ " + Misc.format(commendations));
		p.getPacketSender().sendString(55081, "@red@Loyalty Points: @gre@" + Misc.format((int) loyaltyPoints));
		p.getPacketSender().sendString(55082, "@red@Dung. Tokens: @gre@ " + Misc.format(dungTokens));
		p.getPacketSender().sendString(55083, "@red@Voting Points: @gre@ " + Misc.format(votingPoints));
		p.getPacketSender().sendString(55084, "@red@Slayer Points: @gre@" + Misc.format(slayerPoints));
		p.getPacketSender().sendString(55085, "@red@Pk Points: @gre@" + Misc.format(pkPoints));
		p.getPacketSender().sendString(55086, "@red@Wilderness Killstreak: @gre@"
				+ Misc.format(p.getPlayerKillingAttributes().getPlayerKillStreak()));
		p.getPacketSender().sendString(55087,
				"@red@Wilderness Kills: @gre@" + Misc.format(p.getPlayerKillingAttributes().getPlayerKills()));
		p.getPacketSender().sendString(55088,
				"@red@Wilderness Deaths: @gre@" + Misc.format(p.getPlayerKillingAttributes().getPlayerDeaths()));
		p.getPacketSender().sendString(55089,
				"@red@Arena Victories: @gre@" + Misc.format(p.getDueling().arenaStats[0]));
		p.getPacketSender().sendString(55090, "@red@Arena Losses: @gre@" + Misc.format(p.getDueling().arenaStats[1]));
		p.getPacketSender().sendString(55091, "@red@Tourney Points: @gre@" + Misc.format(tournamentPoints));
*/
		return this;
	}

	private int prestigePoints;
	private int slayerPoints;
	private int commendations;
	private int dungTokens;
	private int pkPoints;
	private double loyaltyPoints;
	private int votingPoints;
	private long tournamentPoints;
	private int achievementPoints;

	public int getPrestigePoints() {
		return prestigePoints;
	}

	public void setPrestigePoints(int points, boolean add) {
		if (add) {
			if (GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.prestigePoints += points;
		} else {
			this.prestigePoints = points;
		}
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public void setSlayerPoints(int slayerPoints, boolean add) {
		if (add) {
			if (GameSettings.DOUBLE_POINTS) {
				slayerPoints *= 2;
			}
			this.slayerPoints += slayerPoints;
		} else {
			this.slayerPoints = slayerPoints;
		}
	}

	public int getCommendations() {
		return this.commendations;
	}

	public void setCommendations(int commendations, boolean add) {
		if (add) {
			if (GameSettings.DOUBLE_POINTS) {
				commendations *= 2;
			}
			this.commendations += commendations;
		} else {
			this.commendations = commendations;
		}
	}

	public int getLoyaltyPoints() {
		return (int) this.loyaltyPoints;
	}

	public long getTournamentPoints() {
		return this.tournamentPoints;
	}

	public void setLoyaltyPoints(int points, boolean add) {
		if (add) {
			if (GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.loyaltyPoints += points;
		} else {
			this.loyaltyPoints = points;
		}
	}

	public void setTournamentPoints(long points, boolean add) {
		if (add) {
			if (GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.tournamentPoints += points;
		} else {
			this.tournamentPoints = points;
		}
	}

	public void incrementLoyaltyPoints(double amount) {
		if (GameSettings.DOUBLE_POINTS) {
			amount *= 2;
		}
		this.loyaltyPoints += amount;
	}

	public void incrementTournamentPoints(long amount) {
		this.tournamentPoints += amount;
	}

	public void incrementPrestigePoints(double amount) {
		if (GameSettings.DOUBLE_POINTS) {
			amount *= 2;
		}
		this.prestigePoints += amount;
	}

	public int getPkPoints() {
		return this.pkPoints;
	}

	public void setPkPoints(int points, boolean add) {
		if (add) {
			this.pkPoints += points;
		} else {
			this.pkPoints = points;
		}
	}

	public int getDungeoneeringTokens() {
		return dungTokens;
	}

	public void setDungeoneeringTokens(int dungTokens, boolean add) {
		if (add)
			this.dungTokens += dungTokens;
		else
			this.dungTokens = dungTokens;
	}

	public int getVotingPoints() {
		return votingPoints;
	}

	public void setVotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}

	public void incrementVotingPoints() {
		this.votingPoints++;
	}

	public void incrementVotingPoints(int amt) {
		this.votingPoints += amt;
	}

	public void setVotingPoints(int points, boolean add) {
		if (add)
			this.votingPoints += points;
		else
			this.votingPoints = points;
	}

	public int getAchievementPoints() {
		return achievementPoints;
	}

	public void setAchievementPoints(int points, boolean add) {
		if (add) {
			if (GameSettings.DOUBLE_POINTS) {
				points *= 2;
			}
			this.achievementPoints += points;
		} else {
			this.achievementPoints = points;
		}
	}
}
