/*
  User: Cloudy
  Date: 02-Mar-19
  Time: 15:04
*/

package cz.cloudy.economysystem.redstonecoding;

import cz.cloudy.economysystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.*;

public class CodingParser {
    private static Object              returnValue = null;
    private static Map<String, Object> variableMap = new LinkedHashMap<>();
//    private static boolean             off, retrn;

    public static boolean isParsable(String[] code) {
        if (!code[0].startsWith("CODE") && !code[0].startsWith("CODER") && !code[0].startsWith("EXT")) {
            return false;
        }
        String[] codes = new String[] {
                code[1],
                code[2],
                code[3]
        };
        return true;
    }

    public static boolean getOutput(Sign source, String[] code, boolean front, boolean left, boolean right,
                                    boolean clear, boolean off) {
        if (!isParsable(code)) return false;
        String[] codes = new String[] {
                code[1],
                code[2],
                code[3]
        };

        System.out.println("START: " + off);
        String valName = null;
        boolean retrn = !off;
        boolean totalBreak = false;
        if (clear) {
//            off = false;
//            retrn = true;
            variableMap.clear();
        }
        int line = 0;
        for (String sCode : codes) {
            if (totalBreak) break;
            String[] sCodes = sCode.split(";");
            for (String s : sCodes) {
                returnValue = null;
                valName = null;
                if (s.startsWith(":") && s.contains("=")) {
                    valName = s.substring(1, s.indexOf(":", 1));
                    s = s.substring(s.indexOf("=") + 1);
                }
                while (s.contains(":") && s.indexOf(":", s.indexOf(":") + 1) > -1) {
                    int start = s.indexOf(":");
                    int end = s.indexOf(":", s.indexOf(":") + 1);
                    String varName = s.substring(start + 1, end);
                    String replace = getVariableValue(varName);
                    s = s.substring(0, start) + replace + s.substring(end + 1);
                }
                int ret = parseCode(source, s, line, front, left, right, off);
//                System.out.println(s + " === " + ret);
                if (ret == -1) continue;
                else if (ret == 0) {
                    off = true;
                    retrn = false;
                    returnValue = 0;
                } else if (ret == -2) {
                    totalBreak = true;
                    retrn = false;
                    break;
                }

                if (valName != null) {
                    if (variableMap.containsKey(valName)) variableMap.replace(valName, returnValue);
                    else variableMap.put(valName, returnValue);
                }
            }
            line++;
        }

        return retrn;
    }

    @SuppressWarnings("Duplicates")
    private static int parseCode(Sign source, String code, int line, boolean front, boolean left, boolean right,
                                 boolean off) {
        if (code.length() == 0) return -1;
        String[] args = code.split(" ");
        if (args[0].equals("IF")) {
            return functionIF(args, front, left, right, off);
        } else if (args[0].equals("ELSE")) {

        } else if (args[0].equals("ELSEIF") || args[0].equals("ELIF")) {

        } else if (args[0].equals("ENDIF") || args[0].equals("ENIF")) {

        } else if (args[0].equals("POWER") || args[0].equals("POW")) {
            return functionPOWER(args, off);
        } else if (args[0].equals("IMPULSE") || args[0].equals("IMP")) {
            return functionIMPULSE(args, off);
        } else if (args[0].equals("RAND")) {
            return functionRAND(args, off);
        } else if (args[0].equals("RANDB")) {
            return functionRANDB(args, off);
        } else if (args[0].equals("KILL") || args[0].equals("!")) {
            return 0;
        } else if (args[0].equals("BITSARRAY") || args[0].equals("BITS[]")) {
            return functionBITSARRAY(args, off);
        } else if (args[0].equals("WAIT")) {
            return functionWAIT(args, source, line, off);
        }

        returnValue = args[0];
        return 1;
    }

    private static Object[][] getSignsByName(String name) {
        List<Object[]> data = new LinkedList<>();
        Sign[] signs = CodingRegister.getSignsByName(name);
        for (Sign sign : signs) {
            Block resultBlock = (Block) CodingRegister.getSignAroundData(sign)[3];
            Block finalResultBlock = (Block) CodingRegister.getSignAroundData(sign)[4];
            if (resultBlock == null) continue;
            data.add(new Object[] {
                    sign,
                    resultBlock,
                    finalResultBlock
            });
        }
        return data.toArray(new Object[0][]);
    }

    private static String getVariableValue(String key) {
        String varName = key;
        int index = -1;
        if (key.contains("[") && key.contains("]")) {
            varName = key.substring(0, key.indexOf("["));
            index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
        }

        if (variableMap.containsKey(varName)) {
            Object data = variableMap.get(varName);
            if (index != -1) {
                Object[] array = (Object[]) data;
                if (array.length <= index) return "0";
                return array[index].toString();
            }
            return data.toString();
        }
        return "0";
    }

    private static int getTicksByTime(String time) {
        int ticks = 0;
        if (time.endsWith("s")) {
            ticks = Integer.parseInt(time.substring(0, time.length() - 1)) * 20;
        } else {
            ticks = Integer.parseInt(time);
        }
        return ticks;
    }


    private static int functionIF(String[] args, boolean front, boolean left, boolean right, boolean off) {
        if (off) return 0;

        boolean ifResult = true;
        int cond = -1;
        for (String arg : args) {
            if (arg.equals("F") || arg.equals("L") || arg.equals("R")) {
                boolean val = arg.equals("F") ? front : (arg.equals("L") ? left : right);
                if (cond == -1) {
                    ifResult = val;
                } else {
                    if (cond == 0 || cond == 1) {
                        ifResult = cond == 0 ? ifResult && val : ifResult || val;
                    } else if (cond == 2) {
                        ifResult = (ifResult && !val) || (!ifResult && val);
                    } else if (cond == 3) {
                        ifResult = !(ifResult && val);
                    }
                    cond = -1;
                }
            } else if (arg.equals("&&") || arg.equals("*") || arg.equals("||") || arg.equals("+") || arg.equals("x") ||
                       arg.equals("n")) {
                if (arg.equals("&&") || arg.equals("*")) {
                    cond = 0;
                } else if (arg.equals("||") || arg.equals("+")) {
                    cond = 1;
                } else if (arg.equals("x")) {
                    cond = 2;
                } else if (arg.equals("n")) {
                    cond = 3;
                }
            } else {
                if (arg.equals("1")) ifResult = true;
                else if (arg.equals("0")) ifResult = false;
                else if (arg.contains("==") || arg.contains("!=") || arg.contains(">") || arg.contains("<") ||
                         arg.contains(">=") || arg.contains("<=")) {
                    char[] chars = new char[] {
                            '=',
                            '!',
                            '<',
                            '>'
                    };
                    int idxStart = -1, idxEnd = -1;
                    for (char aChar : chars) {
                        int pos = arg.indexOf(aChar);
                        if ((pos < idxStart && pos != -1) || idxStart == -1) {
                            idxStart = pos;
                        }
                        int epos = arg.indexOf(aChar, pos + 1);
                        if (epos == -1) epos = pos;
                        if (epos > idxEnd) {
                            idxEnd = epos;
                        }
                    }

                    String[] expr = new String[] {
                            arg.substring(0, idxStart),
                            arg.substring(idxStart, idxEnd + 1),
                            arg.substring(idxEnd + 1)
                    };

                    System.out.println(expr[0] + expr[1] + expr[2]);
                    for (int i = 0; i <= 2; i += 2) {
                        if (expr[i].equals("L")) {
                            expr[i] = left ? "1" : "0";
                        } else if (expr[i].equals("F")) {
                            expr[i] = front ? "1" : "0";
                        } else if (expr[i].equals("R")) {
                            expr[i] = right ? "1" : "0";
                        }
                    }

                    boolean result = false;

                    if (expr[1].equals("==")) {
                        result = expr[0].equals(expr[2]);
                    } else if (expr[1].equals("!=")) {
                        result = !expr[0].equals(expr[2]);
                    } else if (expr[1].equals(">")) {
                        result = Integer.parseInt(expr[0]) > Integer.parseInt(expr[2]);
                    } else if (expr[1].equals("<")) {
                        result = Integer.parseInt(expr[0]) < Integer.parseInt(expr[2]);
                    } else if (expr[1].equals(">=")) {
                        result = Integer.parseInt(expr[0]) >= Integer.parseInt(expr[2]);
                    } else if (expr[1].equals("<=")) {
                        result = Integer.parseInt(expr[0]) <= Integer.parseInt(expr[2]);
                    }

                    ifResult = ifResult && result;
                }
            }
        }

        return ifResult ? 1 : 0;
    }

    @SuppressWarnings("Duplicates")
    private static int functionPOWER(String[] args, boolean off) {
        boolean checkCode = false;
        Object[][] data = getSignsByName(args[1]);
        if (args.length == 3) checkCode = args[2].equals("1");
        for (Object[] datas : data) {
            Sign sign = (Sign) datas[0];
            Block resultBlock = (Block) datas[2];
            if (checkCode) {
                CodingRegister.workSign(sign, true, off);
            } else {
                CodingRegister.setRedstoneTorchLit(resultBlock, !off, CodingRegister.getSignCodeType(sign) == 1);
                CodingRegister.setSignIgnored(sign);
            }
        }

        return 1;
    }

    @SuppressWarnings("Duplicates")
    private static int functionIMPULSE(String[] args, boolean off) {
        if (off) return -1;

        boolean checkCode = false;
        Object[][] data = getSignsByName(args[1]);
        if (args.length == 4) checkCode = args[3].equals("1");
        for (Object[] datas : data) {
            Sign sign = (Sign) datas[0];
            Block resultBlock = (Block) datas[2];
            int ticks = 10;
            if (args.length == 3) {
                ticks = getTicksByTime(args[2]);
            }
            if (ticks < 6) ticks = 6;

            if (checkCode) {
                CodingRegister.workSign(sign, true, false);
            } else {
                CodingRegister.setRedstoneTorchLit(resultBlock, true, CodingRegister.getSignCodeType(sign) == 1);
                CodingRegister.setSignIgnored(sign);
            }
            Bukkit.getServer()
                  .getScheduler()
                  .scheduleSyncDelayedTask(Main.instance, () -> {
                      CodingRegister.setRedstoneTorchLit(resultBlock, false, CodingRegister.getSignCodeType(sign) == 1);
                      CodingRegister.setSignIgnored(sign);
                  }, ticks);
        }

        return 1;
    }

    private static int functionRAND(String[] args, boolean off) {
        if (off) return -1;

        int min = 0, num = 1;
        if (args.length == 2) {
            num = Integer.parseInt(args[1]);
        } else if (args.length == 3) {
            min = Integer.parseInt(args[1]);
            num = Integer.parseInt(args[2]);
        }
        Random random = new Random();
        returnValue = min + random.nextInt(num);
        return 1;
    }

    private static int functionRANDB(String[] args, boolean off) {
        if (off) return -1;

        Random random = new Random();
        return random.nextInt(2);
    }

    private static int functionBITSARRAY(String[] args, boolean off) {
        if (off || args.length != 3) return -1;

        String value = args[1];
        int size = Integer.parseInt(args[2]);
        int number = Integer.parseInt(value);
        String bits = Integer.toBinaryString(number);
        if (bits.length() < size) {
            for (int i = 0; i < size - bits.length(); i++) {
                bits = "0" + bits;
            }
        } else if (bits.length() > size) {
            bits = bits.substring(bits.length() - size);
        }
        System.out.println(bits);
        Integer[] bitsArray = new Integer[bits.length()];
        for (int i = 0; i < bits.length(); i++) {
            bitsArray[i] = Integer.parseInt(bits.substring(i, i + 1));
        }
        returnValue = bitsArray;
        return 1;
    }

    private static int functionWAIT(String[] args, Sign source, int line, boolean off) {
        if (off || args.length != 2) return -1;
        int ticks = getTicksByTime(args[1]);
        int finalLine = ++line;
        Bukkit.getServer()
              .getScheduler()
              .scheduleSyncDelayedTask(Main.instance, () -> {
                  CodingRegister.workSign(source, true, false, new boolean[] {
                          false,
                          false,
                          false
                  }, finalLine);
              }, ticks);

        return -2;
    }
}
