package frc.robot;

import static edu.wpi.first.units.Units.Degrees;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.commands;

public class bindings {

  public static void configure_bindings(robot robot) {
    Supplier<Translation2d> ctrl_strafe = () -> oi.vector_deadband(oi.get_left_stick(oi.drive), oi::strafe);
    Supplier<Double> ctrl_turn = () -> oi.deadband_precise(-oi.drive.getRightX(), oi::strafe);
    robot.swerve.strafe_subsystembase.setDefaultCommand(commands.teleop_swerve_strafe(robot, ctrl_strafe));
    robot.swerve.turn_subsystembase.setDefaultCommand(commands.teleop_swerve_turn(robot, ctrl_turn)); 
    
    var ctrl_reset_heading = oi.cmd_controller.leftBumper();
    var ctrl_reset_turn = oi.cmd_controller.rightBumper(); 
    var ctrl_save_turrent_offsets = oi.cmd_controller.rightTrigger();
    var ctrl_strafe_to_point = oi.cmd_controller.a();
    
    ctrl_reset_heading.onTrue(Commands.runOnce(() -> {
      robot.swerve.zero_heading();
    }).ignoringDisable(true));

    ctrl_reset_turn.onTrue(Commands.runOnce(() -> {
      robot.swerve.reset_turn();
    }).ignoringDisable(true));
    
    ctrl_save_turrent_offsets.onTrue(Commands.runOnce(() -> {
      robot.turret.save_offsets();
    }).ignoringDisable(true));

    var swerve = robot.swerve;
    ctrl_strafe_to_point.whileTrue(robot.swerve.strafe_to_tag(config.shooter_ll).alongWith(swerve.snap(Degrees.of(0))));
  } 

}
