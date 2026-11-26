import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * 测试用例设计总体原则：
 * 1. 采用等价类划分法，以"当前细胞状态（活/死）"和"周围8个方向活细胞数量（0~8）"
 *    两个维度划分等价类，覆盖生命游戏的四条生存定律；
 * 2. 结合边界值分析，覆盖角落、边缘细胞以及 1x1 最小棋盘，验证邻居越界被正确处理；
 * 3. 采用题目给出的两个官方示例进行端到端验证，保证整体演化结果正确。
 */
public class L2024110862_19_Test {

    // 辅助方法：逐行比较两个二维棋盘，失败时能定位到具体行
    private void assertBoardEquals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length, "棋盘行数不一致");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "第" + i + "行内容不一致");
        }
    }

    /*
     * 测试目的：验证规则1，活细胞周围活细胞少于2个时死亡
     * 测试用例：3x3棋盘，仅中心细胞为活，活邻居数=0，预期中心变为死且无细胞复活
     */
    @Test
    public void testLiveCellZeroNeighborDies() {
        int[][] board = {{0,0,0},{0,1,0},{0,0,0}};
        int[][] expected = {{0,0,0},{0,0,0},{0,0,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：验证规则1，活细胞周围仅有1个活细胞时死亡
     * 测试用例：3x3棋盘，左上角和中心为活，中心活邻居数=1，预期两个活细胞均死亡
     */
    @Test
    public void testLiveCellOneNeighborDies() {
        int[][] board = {{1,0,0},{0,1,0},{0,0,0}};
        int[][] expected = {{0,0,0},{0,0,0},{0,0,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：验证规则2，活细胞周围有2个活细胞时继续存活
     * 测试用例：3x3棋盘，中心及左上角、上方为活，中心活邻居数=2；
     *           同时左侧死细胞恰有3个活邻居而复活
     */
    @Test
    public void testLiveCellTwoNeighborsSurvive() {
        int[][] board = {{1,1,0},{0,1,0},{0,0,0}};
        int[][] expected = {{1,1,0},{1,1,0},{0,0,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：验证规则2，活细胞周围有3个活细胞时继续存活
     * 测试用例：3x3棋盘，上排三个及中心为活，中心活邻居数=3；
     *           左右两个死细胞各有3个活邻居而复活
     */
    @Test
    public void testLiveCellThreeNeighborsSurvive() {
        int[][] board = {{1,1,1},{0,1,0},{0,0,0}};
        int[][] expected = {{1,1,1},{1,1,1},{0,0,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：验证规则3，活细胞周围活细胞超过3个时死亡
     * 测试用例：3x3棋盘，中心活邻居数=4，预期中心死亡；左侧死细胞因3个活邻居复活
     */
    @Test
    public void testLiveCellFourNeighborsDies() {
        int[][] board = {{1,1,1},{0,1,1},{0,0,0}};
        int[][] expected = {{1,0,1},{1,0,1},{0,0,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：验证规则4，死细胞周围恰好3个活细胞时复活
     * 测试用例：3x3棋盘，上排三个为活，中心为死且有3个活邻居，预期中心复活
     */
    @Test
    public void testDeadCellThreeNeighborsRevive() {
        int[][] board = {{1,1,1},{0,0,0},{0,0,0}};
        int[][] expected = {{0,1,0},{0,1,0},{0,0,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：验证死细胞周围活细胞数不为3时保持死亡
     * 测试用例：3x3棋盘，左上角两个为活，中心死细胞活邻居数=2，预期无细胞复活
     */
    @Test
    public void testDeadCellTwoNeighborsStaysDead() {
        int[][] board = {{1,1,0},{0,0,0},{0,0,0}};
        int[][] expected = {{0,0,0},{0,0,0},{0,0,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：边界值验证，1x1棋盘内的活细胞因0个邻居死亡
     * 测试用例：board={{1}}，预期{{0}}
     */
    @Test
    public void testSingleLiveCellDies() {
        int[][] board = {{1}};
        int[][] expected = {{0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：边界值验证，1x1棋盘内的死细胞保持死亡且不越界
     * 测试用例：board={{0}}，预期{{0}}
     */
    @Test
    public void testSingleDeadCellStaysDead() {
        int[][] board = {{0}};
        int[][] expected = {{0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：用题目官方示例1做端到端验证
     * 测试用例：4x3棋盘，验证多细胞同时演化的结果
     */
    @Test
    public void testOfficialExample1() {
        int[][] board = {{0,1,0},{0,0,1},{1,1,1},{0,0,0}};
        int[][] expected = {{0,0,0},{1,0,1},{0,1,1},{0,1,0}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }

    /*
     * 测试目的：用题目官方示例2做端到端验证
     * 测试用例：2x2棋盘，右下角死细胞因3个活邻居复活
     */
    @Test
    public void testOfficialExample2() {
        int[][] board = {{1,1},{1,0}};
        int[][] expected = {{1,1},{1,1}};
        new Solution().gameOfLife(board);
        assertBoardEquals(expected, board);
    }
}
